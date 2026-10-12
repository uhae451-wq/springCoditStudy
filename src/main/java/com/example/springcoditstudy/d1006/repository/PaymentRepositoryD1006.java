package com.example.springcoditstudy.d1006.repository;

import com.example.springcoditstudy.d1006.entity.PaymentD1006;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepositoryD1006 extends JpaRepository<PaymentD1006,Long> {

}
