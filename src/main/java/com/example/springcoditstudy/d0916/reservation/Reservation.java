package com.example.springcoditstudy.d0916.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private long id;
    private long trainerId;
    private String memberName;
    private LocalDateTime startTime;

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(), reservation.getTrainerId(),
                reservation.getMemberName(), reservation.getStartTime()
        );
    }
}
