package com.example.springcoditstudy.d1026.dto;

import com.example.springcoditstudy.d1026.exception.ErrorCode;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private String code;
  private String message;
  private List<FieldError> errors;

  public static ErrorResponse of(ErrorCode errorCode) {
    return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), null);
  }

  public static ErrorResponse of(ErrorCode errorCode, List<FieldError> errors) {
    return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), errors);
  }

  @Getter
  @AllArgsConstructor
  public static class FieldError {

    private String field;
    private String message;
  }
}
