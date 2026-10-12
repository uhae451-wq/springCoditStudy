package com.example.springcoditstudy.d1006.exception;

public class OrderNotFoundException extends RuntimeException {

  public OrderNotFoundException(long id){
    super("주문을 찾을 수 없습니다. 요청 아이디 : " + id);
  }
}
