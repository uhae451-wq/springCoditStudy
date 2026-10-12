package com.example.springcoditstudy.d1006.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments_d1006")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentD1006 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private int amount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private PayMethod method = PayMethod.CARD;

  @Column(nullable = false, updatable = false)
  private LocalDateTime paidAt = LocalDateTime.now();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false, unique = true)
  private OrderD1006 orders;

  public PaymentD1006(OrderD1006 orders,int amount, PayMethod payMethod){
    this.orders = orders;
    this.method = payMethod;
    this.amount = amount;
  }

  public enum PayMethod{
    CARD, TRANSFER, POINT
  }

}
