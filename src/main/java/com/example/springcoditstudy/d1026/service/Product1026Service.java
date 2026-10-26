package com.example.springcoditstudy.d1026.service;

import com.example.springcoditstudy.d1026.domain.Product1026;
import com.example.springcoditstudy.d1026.dto.ProductRequest;
import com.example.springcoditstudy.d1026.repository.Product1026Repository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Product1026Service {

  private final Product1026Repository product1026Repository;

  public Product1026 save(ProductRequest productRequest){
    return product1026Repository.save(productRequest.getName(),productRequest.getPrice(),productRequest.getStock());
  }

}
