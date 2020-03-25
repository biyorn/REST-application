package com.epam.esm.service.impl;

import com.epam.esm.dto.BoughtCertificateDTO;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.impl.BoughtCertificate;
import com.epam.esm.entity.impl.Certificate;
import com.epam.esm.entity.impl.Order;
import com.epam.esm.entity.impl.UserEntity;
import com.epam.esm.exception.IncorrectParameterException;
import com.epam.esm.exception.NotFoundObjectException;
import com.epam.esm.mapper.CommonModelMapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.specification.Specification;
import com.epam.esm.specification.orderby.impl.certificate.factory.CertificateOrderByFactory;
import com.epam.esm.specification.predicate.impl.certificate.factory.CertificatePredicateSpecificationFactory;
import com.epam.esm.specification.predicate.impl.order.factory.OrderSpecificationFactory;
import com.epam.esm.specification.predicate.impl.user.factory.UserSpecificationFactory;
import com.epam.esm.validation.PageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 1;

    private OrderSpecificationFactory orderSpecificationFactory;
    private UserSpecificationFactory userSpecificationFactory;
    private CertificatePredicateSpecificationFactory certificatePredicateSpecificationFactory;
    private CertificateRepository certificateRepository;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private CertificateOrderByFactory certificateOrderByFactory;
    private CommonModelMapper<Order, OrderDTO> orderModelMapper;
    private CommonModelMapper<Certificate, CertificateDTO> certificateModelMapper;
    private PageValidator pageValidator;

    @Autowired
    public OrderServiceImpl(OrderSpecificationFactory orderSpecificationFactory,
                            UserSpecificationFactory userSpecificationFactory,
                            CertificatePredicateSpecificationFactory certificatePredicateSpecificationFactory,
                            CertificateRepository certificateRepository,
                            OrderRepository orderRepository,
                            UserRepository userRepository,
                            CertificateOrderByFactory certificateOrderByFactory, CommonModelMapper<Order, OrderDTO> orderModelMapper,
                            CommonModelMapper<Certificate, CertificateDTO> certificateModelMapper,
                            PageValidator pageValidator) {
        this.orderSpecificationFactory = orderSpecificationFactory;
        this.userSpecificationFactory = userSpecificationFactory;
        this.certificatePredicateSpecificationFactory = certificatePredicateSpecificationFactory;
        this.certificateRepository = certificateRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.certificateOrderByFactory = certificateOrderByFactory;
        this.orderModelMapper = orderModelMapper;
        this.certificateModelMapper = certificateModelMapper;
        this.pageValidator = pageValidator;
    }

    @Override
    @Transactional
    public OrderDTO getOrderById(Long id) {
        return orderRepository
                .query(orderSpecificationFactory.getOrderSpecificationById(id), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE)
                .stream()
                .findFirst()
                .map(orderModelMapper::toDto)
                .orElseThrow(() -> new NotFoundObjectException("Not found a order by id - " + id));
    }

    @Override
    @Transactional
    public List<OrderDTO> getAll(int pageNum, int pageSize) {
        pageValidator.verifyPageNumbers(pageNum, pageSize);
        return orderRepository
                .query(orderSpecificationFactory.getOrderSpecificationGetAll(), pageNum, pageSize)
                .stream()
                .map(orderModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<OrderDTO> getUserOrdersByLogin(String login, int pageNum, int pageSize) {
        pageValidator.verifyPageNumbers(pageNum, pageSize);
        UserEntity userEntity = getUser(userSpecificationFactory.getUserSpecificationByLogin(login));
        return userEntity.getOrders().stream()
                .map(orderModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<OrderDTO> getUserOrdersById(Long id, int pageNum, int pageSize) {
        pageValidator.verifyPageNumbers(pageNum, pageSize);
        UserEntity userEntity = getUser(userSpecificationFactory.getUserSpecificationById(id));
        return userEntity.getOrders().stream()
                .map(orderModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO buy(String login, OrderDTO orderDTO) {
        UserEntity userEntity = getUser(userSpecificationFactory.getUserSpecificationByLogin(login));
        Order order = new Order();
        order.setUserEntity(userEntity);

        List<BoughtCertificateDTO> certificates = checkCertificates(orderDTO.getBoughtCertificates());
        List<BoughtCertificate> boughtCertificates = certificates.stream()
                .map(boughtCertificateDTO -> certificateModelMapper.toEntity(boughtCertificateDTO.getCertificate()))
                .map(boughtCertificate -> createBoughtCertificate(order, boughtCertificate))
                .collect(Collectors.toList());

        order.setBoughtCertificates(boughtCertificates);
        order.setCost(identifyCost(boughtCertificates));
        return orderModelMapper.toDto(orderRepository.add(order));
    }

    private UserEntity getUser(Specification<UserEntity> specification) {
        return userRepository
                .query(specification, DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundObjectException("User does not exist."));
    }

    private List<BoughtCertificateDTO> checkCertificates(List<BoughtCertificateDTO> boughtCertificates) {
        if (Objects.nonNull(boughtCertificates) && !boughtCertificates.isEmpty()) {
            boughtCertificates.stream()
                    .map(boughtCertificate -> getCertificateDTO(boughtCertificate.getCertificate()))
                    .forEach(certificateDTO -> {
                        if (!certificateDTO.isActive()) {
                            throw new NotFoundObjectException(
                                    "The certificate [" + certificateDTO.getName() + "] was deleted"
                            );
                        }
                    });
            return boughtCertificates.stream()
                    .map(boughtCertificate -> getCertificateDTO(boughtCertificate.getCertificate()))
                    .map(certificateDTO -> {
                        BoughtCertificateDTO boughtCertificateDTO = new BoughtCertificateDTO();
                        boughtCertificateDTO.setCertificate(certificateDTO);
                        return boughtCertificateDTO;
                    })
                    .collect(Collectors.toList());
        } else {
            throw new IncorrectParameterException("You should add at least 1 certificate");
        }
    }

    private CertificateDTO getCertificateDTO(CertificateDTO certificateDTO) {
        return certificateRepository.query(certificatePredicateSpecificationFactory.
                        getCertificatePredicateSpecificationById(certificateDTO.getId()),
                certificateOrderByFactory.getCertificateOrderBySpecificationDefault(),
                DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE)
                .getList().stream()
                .findFirst().map(certificateModelMapper::toDto)
                .orElseThrow(() ->
                        new NotFoundObjectException("Not found a certificate by id - [" + certificateDTO.getId() + "]"));
    }

    private BoughtCertificate createBoughtCertificate(Order order, Certificate certificate) {
        BoughtCertificate local = new BoughtCertificate();
        local.setOrder(order);
        local.setCertificate(certificate);
        local.setPrice(certificate.getPrice());
        return local;
    }

    private BigDecimal identifyCost(List<BoughtCertificate> boughtCertificates) {
        return boughtCertificates.stream()
                .map(BoughtCertificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
