package com.epam.esm.repository;

import com.epam.esm.entity.impl.BoughtCertificate;
import com.epam.esm.entity.impl.BoughtCertificatePagination;
import com.epam.esm.specification.orderby.OrderBySpecification;
import com.epam.esm.specification.predicate.PredicateSpecification;

public interface BoughtCertificateRepository {

    BoughtCertificatePagination query(PredicateSpecification<BoughtCertificate> predicateSpecification,
                                                         OrderBySpecification<BoughtCertificate> orderBySpecification,
                                                         int pageNum, int pageSize);
}
