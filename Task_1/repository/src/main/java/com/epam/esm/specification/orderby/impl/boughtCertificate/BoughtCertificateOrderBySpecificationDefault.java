package com.epam.esm.specification.orderby.impl.boughtCertificate;

import com.epam.esm.entity.impl.BoughtCertificate;
import com.epam.esm.specification.orderby.OrderBySpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public class BoughtCertificateOrderBySpecificationDefault implements OrderBySpecification<BoughtCertificate> {

    @Override
    public Order toOrderBy(CriteriaBuilder criteriaBuilder, Root<BoughtCertificate> root) {
        return criteriaBuilder.desc(root.get("id"));
    }
}
