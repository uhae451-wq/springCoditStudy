package com.example.springcoditstudy.d1001.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Table(name = "reservations")
@Getter
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime reservedAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ReservationStatus status = ReservationStatus.RESERVED;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "patient_id", nullable = false)
  private Patient patient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "doctor_id", nullable = false)
  private Doctor doctor;

  protected Reservation() {}

  public Reservation(Patient patient, Doctor doctor, LocalDateTime reservedAt) {
    this.patient = patient;
    this.doctor = doctor;
    this.reservedAt = reservedAt;
  }

  public void confirm() {
    if (status != ReservationStatus.RESERVED) {
      throw new IllegalStateException("확정할 수 없는 상태입니다: " + status);
    }
    this.status = ReservationStatus.CONFIRMED;
  }

  public enum ReservationStatus {
    RESERVED, CONFIRMED, DONE, CANCELED, NO_SHOW
  }
}
