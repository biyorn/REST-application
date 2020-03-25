package com.epam.esm.repository;

import com.epam.esm.entity.impl.Order;
import com.epam.esm.specification.Specification;

import java.util.List;

public interface OrderRepository {

    Order add(Order order);

    List<Order> query(Specification<Order> specification, int pageNum, int pageSize);
}
