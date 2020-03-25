package com.epam.esm.specification.predicate.impl.bought;

import com.epam.esm.entity.impl.BoughtCertificate;
import com.epam.esm.entity.impl.UserEntity;
import com.epam.esm.specification.predicate.PredicateSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class BoughtCertificatePredicateSpecificationByUserLogin implements PredicateSpecification<BoughtCertificate> {

    private String login;

    public BoughtCertificatePredicateSpecificationByUserLogin(String login) {
        this.login = login;
    }

    @Override
    public List<Predicate> toPredicate(CriteriaBuilder criteriaBuilder,
                                       CriteriaQuery<?> criteriaQuery,
                                       Root<BoughtCertificate> root) {
        List<Predicate> predicates = new ArrayList<>();
        Join<BoughtCertificate, Order> orderJoin = root.join("order");
        Join<Order, UserEntity> userEntityOrderJoin = orderJoin.join("userEntity");
        predicates.add(criteriaBuilder.equal(userEntityOrderJoin.get("login"), login));
        return predicates;
    }
}
