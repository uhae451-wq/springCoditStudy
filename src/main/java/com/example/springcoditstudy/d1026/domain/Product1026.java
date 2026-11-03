package com.example.springcoditstudy.d1026.domain;

import com.example.springcoditstudy.d1026.exception.ErrorCode;
import com.example.springcoditstudy.d1026.exception.OutOfStockException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Product1026 {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private int price;

  private int stock;

  public Product1026(String name, int price, int stock){
    this.name = name;
    this.price = price;
    this.stock = stock;
  }

  public void removeStock(int quantity){
    if(stock < quantity){
      throw new OutOfStockException(ErrorCode.PRODUCT_OUT_OF_STOCK);
    }
    this.stock -= quantity;
  }
}
