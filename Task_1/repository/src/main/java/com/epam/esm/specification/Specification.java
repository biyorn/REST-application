package com.epam.esm.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public interface Specification<T> {

    CriteriaQuery<T> getQuery(CriteriaBuilder criteriaBuilder);
}
