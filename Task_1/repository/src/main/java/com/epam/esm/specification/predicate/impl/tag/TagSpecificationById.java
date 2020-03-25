package com.epam.esm.specification.predicate.impl.tag;

import com.epam.esm.entity.impl.Tag;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class TagSpecificationById implements Specification<Tag> {

    private Long id;

    public TagSpecificationById(Long id) {
        this.id = id;
    }

    @Override
    public CriteriaQuery<Tag> getQuery(CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        return criteriaQuery
                .select(root)
                .where(criteriaBuilder.equal(root.get("id"), id));
    }
}
