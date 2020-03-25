package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserEntityDTO;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.epam.esm.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private UserServiceImpl userServiceImpl;
    private OrderServiceImpl orderServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, OrderServiceImpl orderServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.orderServiceImpl = orderServiceImpl;
    }

    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserEntityDTO> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userServiceImpl.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/orders")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long id,
                                                        @RequestParam(defaultValue = "1", required = false) int pageNum,
                                                        @RequestParam(defaultValue = "5", required = false) int pageSize) {
        return new ResponseEntity<>(orderServiceImpl.getUserOrdersById(id, pageNum, pageSize), HttpStatus.OK);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserEntityDTO>> getUsers(@RequestParam(defaultValue = "1", required = false) int pageNum,
                                                        @RequestParam(defaultValue = "5", required = false) int pageSize) {
        return new ResponseEntity<>(userServiceImpl.getUsers(pageNum, pageSize), HttpStatus.OK);
    }
}
