package com.example.springcoditstudy.d1006.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler({OutOfStockException.class, IllegalStateException.class,IllegalArgumentException.class})
  public ResponseEntity<Map<String, String>> badRequest(RuntimeException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler({MemberNotFoundException.class, BookNotFoundException.class,OrderNotFoundException.class})
  public ResponseEntity<Map<String, String>> notFound(RuntimeException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
  }
}
