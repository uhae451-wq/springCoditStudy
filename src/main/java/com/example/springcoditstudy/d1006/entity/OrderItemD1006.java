package com.example.springcoditstudy.d1006.entity;

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
@Table(name = "order_items_d1006")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemD1006 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private int orderPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private BookD1006 book;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private OrderD1006 order;

  public OrderItemD1006(BookD1006 book,int quantity){
    this.book = book;
    this.quantity = quantity;
    this.orderPrice = book.getPrice();
  }

  public void assignOrder(OrderD1006 order){
    this.order = order;
  }

}
