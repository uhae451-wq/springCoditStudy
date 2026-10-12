package com.example.springcoditstudy.d1006.exception;

public class OutOfStockException extends RuntimeException {

  public OutOfStockException(String title, int stock, int quantity){
    super("재고 부족 : " + title + "(재고 : " + stock + " / 주문수량 : " + quantity + ")");
  }
}
