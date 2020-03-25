package com.epam.esm.repository.impl;

import com.epam.esm.entity.impl.Certificate;
import com.epam.esm.entity.impl.CertificatePagination;
import com.epam.esm.entity.impl.Tag;
import com.epam.esm.repository.CertificateRepository;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository, Pagination<Certificate> {

    private static final String DEFAULT_SORT = "creation_date";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Certificate add(Certificate certificate) {
        LocalDateTime localDateTime = getCurrentDateTime();
        certificate.setCreationDate(localDateTime);
        certificate.setModificationDate(localDateTime);
        certificate.setActive(true);
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public Certificate update(Long id, Certificate certificate) {
        certificate.setModificationDate(getCurrentDateTime());
        certificate.setId(id);
        entityManager.merge(certificate);
        certificate = entityManager.find(Certificate.class, id);
        return certificate;
    }

    @Override
    public Certificate patch(Long id, Certificate certificate) {
        Certificate local = entityManager.find(Certificate.class, id);
        String name = certificate.getName();
        if (name != null) {
            local.setName(name);
        }
        String description = certificate.getDescription();
        if (description != null) {
            local.setDescription(description);
        }
        BigDecimal price = certificate.getPrice();
        if (price != null) {
            local.setPrice(price);
        }
        int duration = certificate.getDurationDays();
        if (duration != 0) {
            local.setDurationDays(duration);
        }
        Set<Tag> tags = certificate.getTags();
        if (tags != null) {
            local.setTags(tags);
        }
        local.setModificationDate(getCurrentDateTime());
        return local;
    }

    @Override
    public void delete(Long id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        certificate.setActive(false);
    }

    @Override
    public CertificatePagination query(PredicateSpecification<Certificate> predicateSpecification,
                                                    OrderBySpecification orderBySpecification,
                                                    int pageNum, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        List<Predicate> predicates = predicateSpecification.toPredicate(criteriaBuilder, criteriaQuery, root);
        Order order = orderBySpecification.toOrderBy(criteriaBuilder, root);
        TypedQuery<Certificate> query = entityManager.createQuery(criteriaQuery
                .select(root)
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(order));
        int numberElements = getMaxPage(criteriaBuilder, predicateSpecification);
        setPagination(query, pageNum, pageSize);
        return new CertificatePagination(query.getResultList(), numberElements);
    }

    private int getMaxPage(CriteriaBuilder criteriaBuilder, PredicateSpecification<Certificate> predicateSpecification) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        List<Predicate> predicates = predicateSpecification.toPredicate(criteriaBuilder, criteriaQuery, root);
        criteriaQuery
                .select(criteriaBuilder.count(root))
                .where(predicates.toArray(new Predicate[]{}))
                .groupBy(root.get("id"));
        return entityManager.createQuery(criteriaQuery).getResultList().size();
    }

    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }
}
