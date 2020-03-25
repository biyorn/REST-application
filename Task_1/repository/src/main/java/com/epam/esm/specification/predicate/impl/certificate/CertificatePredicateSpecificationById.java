package com.epam.esm.specification.predicate.impl.certificate;

import com.epam.esm.entity.impl.Certificate;
import com.epam.esm.specification.predicate.PredicateSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CertificatePredicateSpecificationById implements PredicateSpecification<Certificate> {

    private Long id;

    public CertificatePredicateSpecificationById(Long id) {
        this.id = id;
    }

    @Override
    public List<Predicate> toPredicate(CriteriaBuilder criteriaBuilder,
                                       CriteriaQuery<?> criteriaQuery,
                                       Root<Certificate> root) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("id"), id));
        return predicates;
    }
}
