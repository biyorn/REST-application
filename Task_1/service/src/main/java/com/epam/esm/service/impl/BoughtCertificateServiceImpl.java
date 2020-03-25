package com.epam.esm.service.impl;

import com.epam.esm.dto.BoughtCertificatePaginationDTO;
import com.epam.esm.entity.impl.BoughtCertificatePagination;
import com.epam.esm.mapper.BoughtCertificatePaginationModelMapper;
import com.epam.esm.repository.BoughtCertificateRepository;
import com.epam.esm.service.BoughtCertificateService;
import com.epam.esm.specification.orderby.impl.boughtCertificate.factory.BoughtCertificateOrderBySpecificationFactory;
import com.epam.esm.specification.predicate.impl.bought.factory.BoughtCertificatePredicateSpecificationFactory;
import com.epam.esm.validation.PageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BoughtCertificateServiceImpl implements BoughtCertificateService {

    private BoughtCertificateRepository boughtCertificateRepository;
    private BoughtCertificateOrderBySpecificationFactory boughtCertificateOrderByFactory;
    private BoughtCertificatePredicateSpecificationFactory boughtCertificatePredicateFactory;
    private BoughtCertificatePaginationModelMapper boughtCertificatePaginationModelMapper;
    private PageValidator pageValidator;

    @Autowired
    public BoughtCertificateServiceImpl(BoughtCertificateRepository boughtCertificateRepository,
                                        BoughtCertificateOrderBySpecificationFactory boughtCertificateOrderByFactory,
                                        BoughtCertificatePredicateSpecificationFactory boughtCertificatePredicateFactory,
                                        BoughtCertificatePaginationModelMapper boughtCertificatePaginationModelMapper,
                                        PageValidator pageValidator) {
        this.boughtCertificateRepository = boughtCertificateRepository;
        this.boughtCertificateOrderByFactory = boughtCertificateOrderByFactory;
        this.boughtCertificatePredicateFactory = boughtCertificatePredicateFactory;
        this.boughtCertificatePaginationModelMapper = boughtCertificatePaginationModelMapper;
        this.pageValidator = pageValidator;
    }

    @Override
    @Transactional
    public BoughtCertificatePaginationDTO getBoughtCertificatesByUserLogin(String login, int pageNum, int pageSize) {
        pageValidator.verifyPageNumbers(pageNum, pageSize);
        BoughtCertificatePagination boughtCertificatePagination = boughtCertificateRepository.query(boughtCertificatePredicateFactory
                .getBoughtCertificatePredicateSpecificationByUser(login),
                boughtCertificateOrderByFactory.getBoughtCertificateOrderBySpecificationDefault(),
                pageNum, pageSize);
        return boughtCertificatePaginationModelMapper.toDto(boughtCertificatePagination);
    }
}
