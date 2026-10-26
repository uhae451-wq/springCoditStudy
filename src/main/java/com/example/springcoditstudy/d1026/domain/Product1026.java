package com.example.springcoditstudy.d1026.domain;

import com.example.springcoditstudy.d1026.exception.ErrorCode;
import com.example.springcoditstudy.d1026.exception.OutOfStockException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product1026 {
  private Long id;
  private String name;
  private int price;
  private int stock;

  public void removeStock(int quantity) {
    if(this.stock < quantity){
      throw new OutOfStockException(ErrorCode.PRODUCT_OUT_OF_STOCK);
    }
    this.stock -= quantity;
  }
}
