package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO getOrderById(Long id);

    List<OrderDTO> getAll(int pageNum, int pageSize);

    List<OrderDTO> getUserOrdersByLogin(String login, int pageNum, int pageSize);

    List<OrderDTO> getUserOrdersById(Long id, int pageNum, int pageSize);

    OrderDTO buy(String login, OrderDTO orderDTO);
}
