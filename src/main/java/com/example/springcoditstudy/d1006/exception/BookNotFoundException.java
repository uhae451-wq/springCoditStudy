package com.example.springcoditstudy.d1006.exception;

public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException(Long id){
    super("책을 찾을 수 없습니다. 요청 아이디 : " + id);
  }
}
