package com.example.springcoditstudy.d1026.repository;

import com.example.springcoditstudy.d1026.domain.Member1026;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Member1026Repository extends JpaRepository<Member1026, Long> {

  boolean existsByEmail(String email);

  default Member1026 save(String name, String email, int age){
    return save(new Member1026(name,email,age));
  }
}
