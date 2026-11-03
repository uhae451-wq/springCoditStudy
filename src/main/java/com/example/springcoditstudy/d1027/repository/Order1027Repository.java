package com.example.springcoditstudy.d1027.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.example.springcoditstudy.d1027.domain.Order1027;

public interface Order1027Repository extends JpaRepository<Order1027, Long> {

    List<Order1027> findByMemberId(Long memberId);

    List<Order1027> findByStatus(
        Order1027.OrderStatus status);
}
