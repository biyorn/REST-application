package com.epam.esm.repository.impl;

import com.epam.esm.entity.impl.BoughtCertificate;
import com.epam.esm.entity.impl.BoughtCertificatePagination;
import com.epam.esm.repository.BoughtCertificateRepository;
import com.epam.esm.repository.Pagination;
import com.epam.esm.specification.orderby.OrderBySpecification;
import com.epam.esm.specification.predicate.PredicateSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class BoughtCertificateRepositoryImpl implements BoughtCertificateRepository, Pagination<BoughtCertificate> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BoughtCertificatePagination query(PredicateSpecification<BoughtCertificate> predicateSpecification,
                                                                OrderBySpecification<BoughtCertificate> orderBySpecification,
                                                                int pageNum, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BoughtCertificate> criteriaQuery = criteriaBuilder.createQuery(BoughtCertificate.class);
        Root<BoughtCertificate> root = criteriaQuery.from(BoughtCertificate.class);
        List<Predicate> predicates = predicateSpecification.toPredicate(criteriaBuilder, criteriaQuery, root);
        Order order = orderBySpecification.toOrderBy(criteriaBuilder, root);
        TypedQuery<BoughtCertificate> query = entityManager.createQuery(criteriaQuery
                .select(root)
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(order));
        int numberElements = getMaxPage(criteriaBuilder, predicateSpecification);
        setPagination(query, pageNum, pageSize);
        return new BoughtCertificatePagination(query.getResultList(), numberElements);
    }

    private int getMaxPage(CriteriaBuilder criteriaBuilder, PredicateSpecification<BoughtCertificate> predicateSpecification) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<BoughtCertificate> root = criteriaQuery.from(BoughtCertificate.class);
        List<Predicate> predicates = predicateSpecification.toPredicate(criteriaBuilder, criteriaQuery, root);
        criteriaQuery
                .select(criteriaBuilder.count(root))
                .where(predicates.toArray(new Predicate[]{}))
                .groupBy(root.get("id"));
        return entityManager.createQuery(criteriaQuery).getResultList().size();
    }
}
