package com.example.springcoditstudy.d0916.reservation;

import java.time.LocalDateTime;

public class ReservationConflictException extends RuntimeException {
    public ReservationConflictException(long trainerId, LocalDateTime startTime) {
        super("해당 트레이너는 " + startTime + "에 이미 예약이 있습니다. " + trainerId);
    }
}
