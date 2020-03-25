package com.epam.esm.service;

import com.epam.esm.dto.BoughtCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.impl.BoughtCertificate;
import com.epam.esm.entity.impl.Order;
import com.epam.esm.entity.impl.UserEntity;
import com.epam.esm.exception.NotFoundObjectException;
import com.epam.esm.mapper.OrderModelMapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.epam.esm.specification.predicate.impl.certificate.factory.CertificatePredicateSpecificationFactory;
import com.epam.esm.specification.predicate.impl.order.factory.OrderSpecificationFactory;
import com.epam.esm.specification.predicate.impl.user.factory.UserSpecificationFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 1;

    private static final Long FIRST_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final BigDecimal FIRST_COST = new BigDecimal(10);
    private static final LocalDateTime LOCAL_DATE_TIME =
            LocalDateTime.of(2020, 2, 8, 5, 14, 1);
    private static final List<BoughtCertificate> BOUGHT_CERTIFICATES =
            Collections.singletonList(new BoughtCertificate());
    private static final List<BoughtCertificateDTO> BOUGHT_CERTIFICATE_DTO_LIST =
            Collections.singletonList(new BoughtCertificateDTO());


    @InjectMocks
    private OrderServiceImpl orderServiceImpl;
    @Mock
    private OrderSpecificationFactory orderSpecificationFactory;
    @Mock
    private UserSpecificationFactory userSpecificationFactory;
    @Mock
    private CertificatePredicateSpecificationFactory certificatePredicateSpecificationFactory;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderModelMapper orderModelMapper;

    private Order firstOrder;
    private OrderDTO firstOrderDto;
    private UserEntity userEntity;


    @Before
    public void init() {
        firstOrder = getOrder(FIRST_ID, FIRST_COST, LOCAL_DATE_TIME, BOUGHT_CERTIFICATES);
        firstOrderDto = getOrderDto(FIRST_ID, FIRST_COST, LOCAL_DATE_TIME, BOUGHT_CERTIFICATE_DTO_LIST);
        userEntity = new UserEntity();
        userEntity.setLogin("Test");
        userEntity.setOrders(Collections.singletonList(firstOrder));
    }

    @Test
    public void getOrderById_FirstIdSupplied_FirstOrderDto() {
        when(orderRepository.query(orderSpecificationFactory
                .getOrderSpecificationById(FIRST_ID), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(firstOrder));
        when(orderModelMapper.toDto(firstOrder)).thenReturn(firstOrderDto);

        OrderDTO actual = orderServiceImpl.getOrderById(FIRST_ID);

        assertEquals(firstOrderDto, actual);
    }

    @Test(expected = NotFoundObjectException.class)
    public void getOrderById_SecondIdSupplied_NotFoundObjectException() {

        when(orderRepository.query(orderSpecificationFactory
                .getOrderSpecificationById(SECOND_ID), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.emptyList());

        orderServiceImpl.getOrderById(SECOND_ID);
    }

    @Test
    public void getUserOrders_SecondLoginUser_FirstOrderSupplied() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(userEntity.getLogin());
        when(userRepository.query(userSpecificationFactory
                .getUserSpecificationByLogin(userEntity.getLogin()), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.singletonList(userEntity));
        when(orderModelMapper.toDto(firstOrder)).thenReturn(firstOrderDto);


        List<OrderDTO> actual = orderServiceImpl.getUserOrdersByLogin(userEntity.getLogin(), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);

        assertEquals(Collections.singletonList(firstOrderDto), actual);
    }

    @Test(expected = NotFoundObjectException.class)
    public void getUserOrders_ThirdUserLogin_Exception() {
        String nonexistent = "incorrect";
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(nonexistent);
        when(userRepository.query(userSpecificationFactory
                .getUserSpecificationByLogin(nonexistent), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE))
                .thenReturn(Collections.emptyList());

        orderServiceImpl.getUserOrdersByLogin(userEntity.getLogin(), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
    }

    private Order getOrder(Long id, BigDecimal cost, LocalDateTime time,
                           List<BoughtCertificate> localBoughtCertificates) {
        Order order = new Order();
        order.setId(id);
        order.setCost(cost);
        order.setTime(time);
        order.setBoughtCertificates(localBoughtCertificates);
        return order;
    }

    private OrderDTO getOrderDto(Long id, BigDecimal cost, LocalDateTime time,
                                 List<BoughtCertificateDTO> localBoughtCertificates) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        orderDTO.setCost(cost);
        orderDTO.setTime(time);
        orderDTO.setBoughtCertificates(localBoughtCertificates);
        return orderDTO;
    }
}
