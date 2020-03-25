package com.epam.esm.specification.predicate.impl.tag;

import com.epam.esm.entity.impl.Tag;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TagSpecificationGetExistTags implements Specification<Tag> {

    private Set<Tag> tags;

    public TagSpecificationGetExistTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public CriteriaQuery<Tag> getQuery(CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        List<String> stringList = tags.stream()
                .map(Tag::getTitle)
                .collect(Collectors.toList());
        return criteriaQuery
                .select(root)
                .where(root.get("title").in(stringList));
    }
}
