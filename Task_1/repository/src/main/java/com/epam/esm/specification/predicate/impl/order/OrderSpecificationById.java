package com.epam.esm.specification.predicate.impl.order;

import com.epam.esm.entity.impl.Order;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

public class OrderSpecificationById implements Specification<Order> {

    private Long id;

    public OrderSpecificationById(Long id) {
        this.id = id;
    }

    @Override
    public CriteriaQuery<Order> getQuery(CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        root.fetch("boughtCertificates", JoinType.INNER);
        return criteriaQuery
                .select(root)
                .where(criteriaBuilder.equal(root.get("id"), id));
    }
}
