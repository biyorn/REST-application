package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.impl.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderModelMapperTest {

    private static final Long ID = 1L;
    private static final BigDecimal COST = new BigDecimal("22.2");

    @InjectMocks
    private OrderModelMapper orderModelMapper;
    @Mock
    private ModelMapper modelMapper;
    private Order order;
    private OrderDTO orderDTO;
    private Class<Order> entity = Order.class;
    private Class<OrderDTO> entityDTO = OrderDTO.class;

    @Before
    public void init() {
        order = getOrder(ID, COST);
        orderDTO = getOrderDTO(ID, COST);
    }

    @Test
    public void toDtoFromEntity_DtoObjectSupplied_Entity() {
        when(modelMapper.map(order, (Type) entityDTO)).thenReturn(orderDTO);

        OrderDTO actual = orderModelMapper.toDto(order);

        assertEquals(orderDTO, actual);
    }

    @Test
    public void toEntityFromDto_EntityObjectSupplied_Dto() {
        when(modelMapper.map(orderDTO, (Type) entity)).thenReturn(order);

        Order actual = orderModelMapper.toEntity(orderDTO);

        assertEquals(order, actual);
    }


    private Order getOrder(Long id, BigDecimal cost) {
        Order local = new Order();
        local.setId(id);
        local.setCost(cost);
        return local;
    }

    private OrderDTO getOrderDTO(Long id, BigDecimal cost) {
        OrderDTO local = new OrderDTO();
        local.setId(id);
        local.setCost(cost);
        return local;
    }
}
