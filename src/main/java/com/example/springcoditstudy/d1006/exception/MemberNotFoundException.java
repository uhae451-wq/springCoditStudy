package com.example.springcoditstudy.d1006.exception;

public class MemberNotFoundException extends RuntimeException {

  public MemberNotFoundException(Long id){
    super("회원을 찾을 수 없습니다. 요청 아이디 : " + id);
  }
}
