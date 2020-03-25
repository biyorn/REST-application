package com.epam.esm.repository.impl;

import com.epam.esm.entity.impl.Tag;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.specification.Specification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository, Pagination<Tag> {

    private static final String SELECT_TAG_FREQUENTLY_USED = "SELECT t.id, t.title FROM orders o " +
            "JOIN users u ON u.id = o.user_id " +
            "JOIN bought_certificate oc ON oc.order_id = o.id " +
            "JOIN certificate c ON oc.certificate_id = c.id " +
            "JOIN tag_certificate tc ON c.id = tc.certificate_id " +
            "JOIN tag t ON t.id = tc.tag_id " +
            "WHERE u.id = (SELECT u.id FROM users u " +
            "JOIN orders o ON u.id = o.user_id " +
            "GROUP BY u.id " +
            "ORDER BY SUM(o.cost) DESC LIMIT 1) " +
            "GROUP BY t.id " +
            "ORDER BY SUM(o.cost) DESC LIMIT 1";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Tag add(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public void delete(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        entityManager.remove(tag);
    }

    @Override
    public Tag frequentlyUsedOrders() {
        List<Object[]> rows = entityManager.createNativeQuery(SELECT_TAG_FREQUENTLY_USED).getResultList();
        return getTag(rows);
    }

    @Override
    public List<Tag> query(Specification<Tag> specification, int pageNum, int pageSize) {
        TypedQuery<Tag> query = entityManager.createQuery(specification.getQuery(entityManager.getCriteriaBuilder()));
        setPagination(query, pageNum, pageSize);
        return query.getResultList();
    }

    private Tag getTag(List<Object[]> rows) {
        Tag tag = new Tag();
        rows.forEach(value -> {
            tag.setId(Long.parseLong(value[0].toString()));
            tag.setTitle(value[1].toString());
        });
        return tag;
    }
}