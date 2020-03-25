package com.epam.esm.repository.impl;

import com.epam.esm.entity.impl.Order;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.Pagination;
import com.epam.esm.specification.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository, Pagination<Order> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Order add(Order order) {
        order.setTime(getCurrentDateTime());
        entityManager.persist(order);
        return order;
    }

    @Override
    public List<Order> query(Specification<Order> specification, int pageNum, int pageSize) {
        TypedQuery<Order> query = entityManager.createQuery(specification.getQuery(entityManager.getCriteriaBuilder()));
        setPagination(query, pageNum, pageSize);
        return query.getResultList();
    }

    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }
}
