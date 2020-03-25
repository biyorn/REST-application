package com.epam.esm.specification.orderby;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public interface OrderBySpecification<T> {

    Order toOrderBy(CriteriaBuilder criteriaBuilder, Root<T> root);
}
