package com.example.springcoditstudy.d1006.controller;

import com.example.springcoditstudy.d1006.dto.OrderRequest;
import com.example.springcoditstudy.d1006.dto.OrderResponse;
import com.example.springcoditstudy.d1006.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<Long> order(@RequestBody OrderRequest request) {
    Long orderId = orderService.order(request.getMemberId(), request.getLines(), request.getMethod());
    return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
  }

  @GetMapping("/{orderId}")
  public OrderResponse get(@PathVariable Long orderId) {
    return orderService.getOrder(orderId);
  }

  @GetMapping
  public List<OrderResponse> list() {
    return orderService.getOrders();
  }

  @DeleteMapping("/{orderId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancel(@PathVariable Long orderId) {
    orderService.cancel(orderId);
  }
}
