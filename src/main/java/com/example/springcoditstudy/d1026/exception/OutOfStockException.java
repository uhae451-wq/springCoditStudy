package com.example.springcoditstudy.d1026.exception;

public class OutOfStockException extends BaseException{

  public OutOfStockException(ErrorCode errorCode) {
    super(errorCode);
  }

}
