package com.epam.esm.specification.predicate.impl.order.factory;

import com.epam.esm.specification.predicate.impl.order.OrderSpecificationById;
import com.epam.esm.specification.predicate.impl.order.OrderSpecificationGetAll;
import org.springframework.stereotype.Component;

@Component
public class OrderSpecificationFactory {

    public OrderSpecificationById getOrderSpecificationById(Long id) {
        return new OrderSpecificationById(id);
    }

    public OrderSpecificationGetAll getOrderSpecificationGetAll() {
        return new OrderSpecificationGetAll();
    }
}
