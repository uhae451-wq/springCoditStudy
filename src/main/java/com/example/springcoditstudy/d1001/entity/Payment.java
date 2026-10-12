package com.example.springcoditstudy.d1001.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private int amount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private PaymentStatus status;

  @Column(nullable = false)
  private LocalDateTime paidAt = LocalDateTime.now();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id", nullable = false, unique = true)
  private Reservation reservation;

  protected Payment() {}

  public Payment(Reservation reservation, int amount, PaymentStatus status) {
    this.reservation = reservation;
    this.amount = amount;
    this.status = status;
  }

  public enum PaymentStatus {
    PAID
  }
}