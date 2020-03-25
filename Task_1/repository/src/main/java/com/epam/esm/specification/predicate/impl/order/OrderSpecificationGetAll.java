package com.epam.esm.specification.predicate.impl.order;

import com.epam.esm.entity.impl.Order;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

public class OrderSpecificationGetAll implements Specification<Order> {

    @Override
    public CriteriaQuery<Order> getQuery(CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        root.fetch("boughtCertificates", JoinType.INNER);
        return criteriaQuery
                .select(root)
                .orderBy(criteriaBuilder.asc(root.get("id")));
    }
}
