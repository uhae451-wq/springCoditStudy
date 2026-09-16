package com.example.springcoditstudy.d0916.reservation;

import com.example.springcoditstudy.d0916.common.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Tag(name = "트레이너 예약",description = "트레이너 예약 등록 API")
    @PostMapping
    public ResponseEntity<ApiResponse<Reservation>> create(@Valid @RequestBody CreateReservationRequest createReservationRequest){
        Reservation reservation = reservationService.createReservation(createReservationRequest.getTrainerId(),
                                                                        createReservationRequest.getMemberName(),
                                                                        createReservationRequest.getStartTime());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(reservation));
    }

    @Tag(name = "트레이너 예약 검색",description = "Reservation ID검색")
    @GetMapping("/{reservation-id}")
    public ResponseEntity<ApiResponse<ReservationResponse>>resgistReservation(@PathVariable("reservation-id")long id){
        Reservation reservation = reservationService.getReservation(id);
        return ResponseEntity.ok().body(ApiResponse.success(ReservationResponse.form(reservation)));
    }

    @Tag(name = "트레이너 예약 검색",description = "전체 및 특정 트레이너ID로 예약 검색")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getAllReservation(@RequestParam(required = false)Long trainerId){
        List<ReservationResponse> reservations = reservationService.getReservations(trainerId).stream()
                .map(Reservation::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(reservations));
    }

    @Tag(name = "예약 시간 변경")
    @PatchMapping("/{reservation-id}")
    public ResponseEntity<ApiResponse<ReservationResponse>> update(@Valid @RequestBody UpdateReservationRequest updateReservationRequest,
                                                                   @PathVariable("reservation-id")long id){
        Reservation reservation = reservationService.updateReservation(id,updateReservationRequest);
        return ResponseEntity.ok().body(ApiResponse.success(Reservation.from(reservation)));
    }

    @Tag(name = "예약 삭제")
    @DeleteMapping("/{reservation-id}")
    public ResponseEntity<Void> delete(@PathVariable("reservation-id")long id){
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }


}
