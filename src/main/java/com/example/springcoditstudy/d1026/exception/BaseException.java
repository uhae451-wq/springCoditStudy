package com.example.springcoditstudy.d1026.exception;

public abstract class BaseException extends RuntimeException {

  private final ErrorCode errorCode;

  protected BaseException(ErrorCode errorcode){
    super(errorcode.getMessage());
    this.errorCode = errorcode;
  }

  public ErrorCode getErrorCode(){
    return errorCode;
  }
}
