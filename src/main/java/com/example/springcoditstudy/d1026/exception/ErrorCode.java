package com.example.springcoditstudy.d1026.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  MEMBER_NOT_FOUND("MEM_001","회원을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
  DUPLICATE_EMAIL("MEM_002","이미 가입된 이메일 입니다.",HttpStatus.CONFLICT),
  DUPLICATE_PHONE("MEM_003","이미 가입된 번호입니다..",HttpStatus.CONFLICT),


  PRODUCT_NOT_FOUND("PROD_001","상품을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
  PRODUCT_OUT_OF_STOCK("PROD_002","현재 재고보다 많은 수량이 주문 되었습니다.",HttpStatus.CONFLICT),

  // 공통
  INVALID_INPUT("CMN_001", "입력값이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
  INTERNAL_ERROR("CMN_999", "서버에 문제가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR)
  ;

  private final String code;

  private final String message;

  private final HttpStatus httpStatus;
}
