package com.example.springcoditstudy.d1001.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Table(name = "records")
@Getter
public class MedicalRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 500)
  private String symptom;

  @Column(length = 1000)
  private String opinion;

  @Column(nullable = false)
  private LocalDateTime writtenAt = LocalDateTime.now();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id", nullable = false, unique = true)
  private Reservation reservation;

  @OneToMany(mappedBy = "record")
  private List<Prescription> prescriptions = new ArrayList<>();

  protected MedicalRecord() {}

  public MedicalRecord(Reservation reservation, String symptom, String opinion) {
    this.reservation = reservation;
    this.symptom = symptom;
    this.opinion = opinion;
  }
}