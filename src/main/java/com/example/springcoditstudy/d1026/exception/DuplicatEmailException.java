package com.example.springcoditstudy.d1026.exception;

public class DuplicatEmailException extends BaseException{

  public DuplicatEmailException(ErrorCode errorCode){
    super(errorCode);
  }

}
