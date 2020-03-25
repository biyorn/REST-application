package com.epam.esm.specification.predicate.impl.tag;

import com.epam.esm.entity.impl.Tag;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class TagSpecificationGetAll implements Specification<Tag> {

    @Override
    public CriteriaQuery<Tag> getQuery(CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        return criteriaQuery
                .select(root)
                .orderBy(criteriaBuilder.asc(root.get("id")));
    }
}
