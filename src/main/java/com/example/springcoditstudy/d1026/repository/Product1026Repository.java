package com.example.springcoditstudy.d1026.repository;

import com.example.springcoditstudy.d1026.domain.Product1026;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class Product1026Repository {
  private final Map<Long, Product1026> store = new ConcurrentHashMap<>();
  private final AtomicLong sequence = new AtomicLong();

  public Product1026 save(String name, int price, int stock) {
    Long id = sequence.incrementAndGet();
    Product1026 Product1026 = new Product1026(id, name, price, stock);
    store.put(id, Product1026);
    return Product1026;
  }

  public Optional<Product1026> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }

}
