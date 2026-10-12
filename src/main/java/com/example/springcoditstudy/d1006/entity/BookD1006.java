package com.example.springcoditstudy.d1006.entity;

import com.example.springcoditstudy.d1006.exception.OutOfStockException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books_d1006")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookD1006 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, length = 200)
  private String title;

  @Column(nullable = false, length = 100)
  private String author;

  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private int stock;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private CategoryD1006 category;

  public BookD1006(String title, String author, int price, int stock){
    this.title = title;
    this.author = author;
    this.price = price;
    this.stock = stock;
  }

  public void removeStock(int quantity){
    int rest = stock - quantity;
    if(rest < 0){
      throw new OutOfStockException(this.title,this.stock,quantity);
    }
    this.stock = rest;
  }

  public void addStock(int quantity){
    this.stock += quantity;
  }

  public void assignCategory(CategoryD1006 category){
    this.category = category;
  }

}
