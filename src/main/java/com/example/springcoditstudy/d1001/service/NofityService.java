package com.example.springcoditstudy.d1001.service;

import com.example.springcoditstudy.d1001.entity.NotifyLog;
import com.example.springcoditstudy.d1001.entity.Reservation;
import com.example.springcoditstudy.d1001.repository.NotifyLogRepository;
import com.example.springcoditstudy.d1001.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NofityService {

  private final NotifyLogRepository notifyLogRepository;
  private final ReservationRepository reservationRepository;


  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void record (Long reservationId, String message){
    Reservation reservation = reservationRepository.getReferenceById(reservationId);
    notifyLogRepository.save(new NotifyLog(reservation,message));
  }
}
