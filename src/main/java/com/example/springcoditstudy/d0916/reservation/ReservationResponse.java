package com.example.springcoditstudy.d0916.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private Long trainerId;
    private String memberName;
    private LocalDateTime startTime;

    public static ReservationResponse form(Reservation reservation){
        return new ReservationResponse(reservation.getId(),reservation.getTrainerId(),reservation.getMemberName(),reservation.getStartTime());
    }
}
