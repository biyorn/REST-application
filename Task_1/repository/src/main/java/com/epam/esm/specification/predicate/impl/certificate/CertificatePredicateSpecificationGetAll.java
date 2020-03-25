package com.epam.esm.specification.predicate.impl.certificate;

import com.epam.esm.entity.impl.Certificate;
import com.epam.esm.specification.predicate.PredicateSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CertificatePredicateSpecificationGetAll implements PredicateSpecification<Certificate> {

    @Override
    public List<Predicate> toPredicate(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery,
                                       Root<Certificate> root) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.isTrue(root.get("active")));
        return predicates;
    }
}
