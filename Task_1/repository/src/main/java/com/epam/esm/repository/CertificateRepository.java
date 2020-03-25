package com.epam.esm.repository;

import com.epam.esm.entity.impl.Certificate;
import com.epam.esm.entity.impl.CertificatePagination;
import com.epam.esm.specification.orderby.OrderBySpecification;
import com.epam.esm.specification.predicate.PredicateSpecification;

public interface CertificateRepository {

    Certificate add(Certificate certificate);

    void delete(Long id);

    Certificate update(Long id, Certificate certificate);

    Certificate patch(Long id, Certificate certificate);

    CertificatePagination query(PredicateSpecification<Certificate> predicateSpecification, OrderBySpecification orderBySpecification,
                                             int pageNum, int pageSize);
}
