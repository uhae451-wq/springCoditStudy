package com.example.springcoditstudy.d1001.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.hibernate.annotations.Audited.Table;

@Entity
@Table(name = "prescriptions")
@Getter
public class Prescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String dosage;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "record_id", nullable = false)
  private MedicalRecord record;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "medicine_id", nullable = false)
  private Medicine medicine;

  protected Prescription() {}

  public Prescription(MedicalRecord record, Medicine medicine, String dosage) {
    this.record = record;
    this.medicine = medicine;
    this.dosage = dosage;
    record.getPrescriptions().add(this);
  }
}
