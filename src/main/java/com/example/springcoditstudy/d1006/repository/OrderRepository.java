package com.example.springcoditstudy.d1006.repository;

import com.example.springcoditstudy.d1006.entity.OrderD1006;
import com.example.springcoditstudy.d1006.entity.OrderD1006.OrderStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderD1006,Long> {

  List<OrderD1006> findByMemberId(Long memberId);

  List<OrderD1006> findByStatus(OrderStatus status);


}
