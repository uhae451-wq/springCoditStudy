package com.example.springcoditstudy.d0915.responseTemplate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultGlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handlerException(CustomException customException){
        // 커스텀 예외가 발생하면 여기로 이동된다.
        // 예외 발생 내용을 채워줌
        ApiError apiError = new ApiError("이유","메세지에러임");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(apiError));
    }
}
