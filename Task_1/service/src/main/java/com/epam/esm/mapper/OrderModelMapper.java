package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.impl.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderModelMapper extends CommonModelMapper<Order, OrderDTO> {

    @Autowired
    public OrderModelMapper(ModelMapper modelMapper) {
        super(modelMapper, Order.class, OrderDTO.class);
    }
}
