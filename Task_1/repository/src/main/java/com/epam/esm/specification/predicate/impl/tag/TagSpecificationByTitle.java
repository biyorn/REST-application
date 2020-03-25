package com.epam.esm.specification.predicate.impl.tag;

import com.epam.esm.entity.impl.Tag;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class TagSpecificationByTitle implements Specification<Tag> {

    private String title;

    public TagSpecificationByTitle(String title) {
        this.title = title;
    }

    @Override
    public CriteriaQuery<Tag> getQuery(CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        return criteriaQuery
                .select(root)
                .where(criteriaBuilder.equal(root.get("title"), title));
    }
}
