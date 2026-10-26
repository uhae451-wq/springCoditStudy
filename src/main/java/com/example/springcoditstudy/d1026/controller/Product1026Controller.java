package com.example.springcoditstudy.d1026.controller;

import com.example.springcoditstudy.d1026.domain.Product1026;
import com.example.springcoditstudy.d1026.dto.ProductRequest;
import com.example.springcoditstudy.d1026.service.Product1026Service;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class Product1026Controller {

  private final Product1026Service product1026Service;

  @PostMapping
  public Product1026 order(@RequestBody ProductRequest request) {
    return product1026Service.save(request);
  }
}
