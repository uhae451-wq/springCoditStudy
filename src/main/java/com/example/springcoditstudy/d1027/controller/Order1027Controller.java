package com.example.springcoditstudy.d1027.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.springcoditstudy.d1027.dto.Order1027Request;
import com.example.springcoditstudy.d1027.dto.Order1027Response;
import com.example.springcoditstudy.d1027.service.Order1027Service;

@RestController
@RequestMapping("/d1027/v1/orders")
@RequiredArgsConstructor
public class Order1027Controller {

    private final Order1027Service orderService;

    @PostMapping
    public ResponseEntity<Order1027Response> order(@Valid @RequestBody Order1027Request request) {
        Long orderId = orderService.order(
                request.getMemberId(), request.getLines(), request.getMethod());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.getOrder(orderId));
    }

    @GetMapping("/{orderId}")
    public Order1027Response get(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping
    public List<Order1027Response> list() {
        return orderService.getOrders();
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long orderId) {
        orderService.cancel(orderId);
    }
}
