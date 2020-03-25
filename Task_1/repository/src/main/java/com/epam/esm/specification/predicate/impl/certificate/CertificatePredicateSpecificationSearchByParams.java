package com.epam.esm.specification.predicate.impl.certificate;

import com.epam.esm.entity.impl.Certificate;
import com.epam.esm.entity.impl.SearchParams;
import com.epam.esm.entity.impl.Tag;
import com.epam.esm.specification.predicate.PredicateSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CertificatePredicateSpecificationSearchByParams implements PredicateSpecification<Certificate> {

    private SearchParams searchParams;

    public CertificatePredicateSpecificationSearchByParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    @Override
    public List<Predicate> toPredicate(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery,
                                       Root<Certificate> root) {
        List<Predicate> predicates = new ArrayList<>();
        String partName = searchParams.getPartName();
        if (Objects.nonNull(partName)) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + partName + "%"));
        }
        List<String> tags = searchParams.getTagName();
        if (Objects.nonNull(tags)) {
            predicates.add(getSearchBySeveralCondition(criteriaBuilder, root, tags));
            criteriaQuery.groupBy(root.get("id"));
            criteriaQuery.having(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.count(root.get("id")),
                    Long.parseLong(String.valueOf(tags.size()))));
        }
        predicates.add(criteriaBuilder.isTrue(root.get("active")));
        return predicates;
    }

    private Predicate getSearchBySeveralCondition(CriteriaBuilder criteriaBuilder,
                                                  Root<Certificate> root,
                                                  List<String> titles) {
        Join<Certificate, Tag> tagJoin = root.join("tags");
        List<Predicate> predicates = new ArrayList<>();
        titles.forEach(tagName -> predicates.add(criteriaBuilder.equal(tagJoin.get("title"), tagName)));
        return criteriaBuilder.or(predicates.toArray(new Predicate[]{}));
    }
}
