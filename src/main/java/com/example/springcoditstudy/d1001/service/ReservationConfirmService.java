package com.example.springcoditstudy.d1001.service;

import com.example.springcoditstudy.d1001.entity.Reservation;
import com.example.springcoditstudy.d1001.repository.NotifyLogRepository;
import com.example.springcoditstudy.d1001.repository.PaymentRepository;
import com.example.springcoditstudy.d1001.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationConfirmService {
  private final PaymentRepository paymentRepository;
  private final ReservationRepository reservationRepository;
  private final NotifyLogRepository notifyLogRepository;
  private final PaymentService paymentService;
  private final NofityService nofityService;







  // 결제를 기록
  // 예약상태 남기기
  // 알람 기록을 남기기
  // 동작들을 하나로 묶어준다.
  @Transactional
  public void conform(Long reservationId, int amount,boolean check) {

    paymentService.payment(reservationId,amount);

    Reservation reservation1 = reservationRepository.findById(reservationId).orElseThrow();

    reservation1.confirm();
    reservationRepository.save(reservation1);

    if(check){
      // REQUIRES_NEW로 인해서 트랜잭션을 별도로 진행됨.
      nofityService.record(reservationId,"장애발생");
      throw new RuntimeException("장애발생");
    }else{
      // REQUIRES_NEW로 인해서 트랜잭션을 별도로 진행됨.
      nofityService.record(reservationId,"결재완료");
    }
  }

}
