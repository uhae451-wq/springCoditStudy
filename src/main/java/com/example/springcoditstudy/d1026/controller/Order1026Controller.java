package com.example.springcoditstudy.d1026.controller;

import com.example.springcoditstudy.d1026.dto.OrderRequest;
import com.example.springcoditstudy.d1026.service.Order1026Service;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class Order1026Controller {

  private final Order1026Service orderService;

  @PostMapping
  public Map<String, String> order(@Valid @RequestBody OrderRequest request) {
    String result = orderService.order(request.getMemberId(), request.getLines());
    return Map.of("message", result);
  }
}
