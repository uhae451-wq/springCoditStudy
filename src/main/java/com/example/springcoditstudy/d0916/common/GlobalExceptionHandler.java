package com.example.springcoditstudy.d0916.common;

import com.example.springcoditstudy.d0916.reservation.ReservationConflictException;
import com.example.springcoditstudy.d0916.reservation.ReservationNotFoundException;
import com.example.springcoditstudy.d0916.trainer.TrainerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>>handlerValidation(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldErrors().stream().
                map(fe -> fe.getField() + " : " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(new ApiError("INVALID_INPUT",message)));
    }

    @ExceptionHandler(TrainerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> trainerNotFoundException(TrainerNotFoundException e){
        ApiError apiError = new ApiError("TRAINER_NOT_FOUND",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(apiError));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> reservationNotFoundException(ReservationNotFoundException e){
        ApiError apiError = new ApiError("RESERVATION_NOT_FOUND",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(apiError));
    }

    @ExceptionHandler(ReservationConflictException.class)
    public ResponseEntity<ApiResponse<Void>> reservationConflictException(ReservationConflictException e){
        ApiError apiError = new ApiError("RESERVATION_CONFLICT",e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.fail(apiError));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> illegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(new ApiError("INVALID_FILE",e.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new ApiError("INTERNAL_ERROR", e.getMessage())));
    }




}
