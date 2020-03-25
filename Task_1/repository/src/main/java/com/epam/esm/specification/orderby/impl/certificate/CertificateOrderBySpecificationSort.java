package com.epam.esm.specification.orderby.impl.certificate;

import com.epam.esm.entity.CertificateSortEnum;
import com.epam.esm.entity.impl.Certificate;
import com.epam.esm.specification.orderby.OrderBySpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public class CertificateOrderBySpecificationSort implements OrderBySpecification<Certificate> {

    private CertificateSortEnum sort;

    public CertificateOrderBySpecificationSort(CertificateSortEnum sort) {
        this.sort = sort;
    }

    @Override
    public Order toOrderBy(CriteriaBuilder criteriaBuilder, Root<Certificate> root) {
        Expression<Certificate> expression = root.get(sort.getTitle());
        if (sort.isAsc()) {
            return criteriaBuilder.asc(expression);
        } else {
            return criteriaBuilder.desc(expression);
        }
    }
}
