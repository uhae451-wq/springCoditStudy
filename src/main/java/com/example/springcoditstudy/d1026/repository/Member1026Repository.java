package com.example.springcoditstudy.d1026.repository;

import com.example.springcoditstudy.d1026.domain.Member1026;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class Member1026Repository {
  private final Map<Long, Member1026> store = new ConcurrentHashMap<>();
  private final AtomicLong sequence = new AtomicLong();

  public Member1026 save(String name, String email, int age) {
    Long id = sequence.incrementAndGet();
    Member1026 Member1026 = new Member1026(id, name, email, age);
    store.put(id, Member1026);
    return Member1026;
  }

  public Optional<Member1026> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }

  public boolean existsByEmail(String email) {
    return store.values().stream()
        .anyMatch(m -> m.getEmail().equals(email));
  }

  public int count(){
    return store.size();
  }
}
