package com.example.springcoditstudy.d1001.service;

import com.example.springcoditstudy.d1001.entity.Payment;
import com.example.springcoditstudy.d1001.entity.Payment.PaymentStatus;
import com.example.springcoditstudy.d1001.entity.Reservation;
import com.example.springcoditstudy.d1001.PaymentFiledException;
import com.example.springcoditstudy.d1001.repository.PaymentRepository;
import com.example.springcoditstudy.d1001.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final ReservationRepository reservationRepository;

  @Transactional(noRollbackFor = PaymentFiledException.class, propagation = Propagation.REQUIRES_NEW)
  public void payment(Long reservationId, int amount) {
    // 예약 찾기
    Reservation reservation = reservationRepository.getReferenceById(reservationId);
    // 예약번호를 찾고 결제 기록을 남김
    paymentRepository.save(new Payment(reservation,amount, PaymentStatus.PAID));

    if(amount < 1000){
      throw new PaymentFiledException("잔액이 부족합니다..");
    }
  }
}
