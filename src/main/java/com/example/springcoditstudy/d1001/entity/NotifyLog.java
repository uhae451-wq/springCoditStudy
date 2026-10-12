package com.example.springcoditstudy.d1001.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notify_logs")
@Getter
public class NotifyLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String message;

  @Column(nullable = false)
  private LocalDateTime sentAt = LocalDateTime.now();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id", nullable = false)
  private Reservation reservation;

  protected NotifyLog() {}

  public NotifyLog(Reservation reservation, String message) {
    this.reservation = reservation;
    this.message = message;
  }
}