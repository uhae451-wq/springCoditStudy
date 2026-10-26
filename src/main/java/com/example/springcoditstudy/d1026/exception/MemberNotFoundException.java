package com.example.springcoditstudy.d1026.exception;

public class MemberNotFoundException extends BaseException {

  public MemberNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
