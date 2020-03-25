package com.epam.esm.specification.orderby.impl.certificate;

import com.epam.esm.entity.impl.Certificate;
import com.epam.esm.specification.orderby.OrderBySpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public class CertificateOrderBySpecificationDefault implements OrderBySpecification<Certificate> {

    @Override
    public Order toOrderBy(CriteriaBuilder criteriaBuilder, Root<Certificate> root) {
        return criteriaBuilder.asc(root.get("creationDate"));
    }
}
