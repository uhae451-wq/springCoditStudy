package com.example.springcoditstudy.d1026.exception;

public class ProductNotFoundException extends BaseException{

  public ProductNotFoundException(ErrorCode errorCode){
    super(errorCode);
  }

}
