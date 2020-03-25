package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrderController {

    private static final String USER_ROLE = "ROLE_USER";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    private OrderServiceImpl service;

    @Autowired
    public OrderController(OrderServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return new ResponseEntity<>(service.getOrderById(id), HttpStatus.OK);
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<OrderDTO>> getUserOrders(@RequestParam(defaultValue = "1", required = false) int pageNum,
                                                        @RequestParam(defaultValue = "5", required = false) int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        List<String> roles = getRoles(authentication);
        List<OrderDTO> orders = roles.contains(USER_ROLE) && !roles.contains(ADMIN_ROLE)
                ? service.getUserOrdersByLogin(login, pageNum, pageSize)
                : service.getAll(pageNum, pageSize);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<OrderDTO> buy(@RequestBody OrderDTO orderDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.buy(authentication.getName(), orderDTO), HttpStatus.CREATED);
    }

    private List<String> getRoles(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
