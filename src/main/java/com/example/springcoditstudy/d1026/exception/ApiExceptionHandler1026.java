package com.example.springcoditstudy.d1026.exception;

import com.example.springcoditstudy.d1026.dto.ErrorResponse;
import com.example.springcoditstudy.d1026.dto.ErrorResponse.FieldError;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.example.springcoditstudy.d1026")
@Slf4j
public class ApiExceptionHandler1026 {

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResponse> handleException (BaseException exception){
    ErrorCode errorCode = exception.getErrorCode();
    log.warn("에러 발생 / 에러 코드 = {}, 에러 메세지 = {}",errorCode.getCode(),errorCode.getMessage());
    return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResponse.of(errorCode));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {

    List<FieldError> errors = e.getBindingResult().getFieldErrors().stream()
        .map(fe -> new ErrorResponse.FieldError(fe.getField(), fe.getDefaultMessage()))
        .toList();

    log.warn("입력값 검증 실패 {}", errors.stream()
        .map(fe -> fe.getField() + "=" + fe.getMessage())
        .toList());

    return ResponseEntity.status(ErrorCode.INVALID_INPUT.getHttpStatus())
        .body(ErrorResponse.of(ErrorCode.INVALID_INPUT, errors));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {

    // 예상하지 못한 예외 — 마지막 인자로 예외를 넘기면 스택트레이스까지 남습니다
    log.error("예상하지 못한 예외가 발생했습니다", e);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of(ErrorCode.INTERNAL_ERROR));
  }
}
