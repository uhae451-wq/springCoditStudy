package com.example.springcoditstudy.d1001.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patients")
@Getter
public class Patient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  @Setter
  @Column(nullable = false, length = 20, unique = true)
  private String phone;

  private LocalDate birthDate;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Transient
  private int age;

  @OneToMany(mappedBy = "patient")
  private List<Allergy> allergies = new ArrayList<>();

  protected Patient() {}

  public Patient(String name, String phone, LocalDate birthDate) {
    this.name = name;
    this.phone = phone;
    this.birthDate = birthDate;
  }
}