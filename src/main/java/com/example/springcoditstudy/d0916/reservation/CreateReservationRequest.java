package com.example.springcoditstudy.d0916.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateReservationRequest {

    @NotNull
    private Long trainerId;

    @NotBlank
    @Schema(description = "예약자 성함", example = "피카츄")
    private String memberName;

    @NotNull
    @Future
    @Schema(description = "예약날짜 및 시간", example = "2026-09-23T19:19:56.672701")
    private LocalDateTime startTime;
}
