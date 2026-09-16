package com.example.springcoditstudy.d0916.reservation;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long id) {
        super("해당 예약을 조회할 수 업습니다. " + id);
    }
}
