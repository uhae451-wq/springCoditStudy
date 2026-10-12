package com.example.springcoditstudy.d1001.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "medicines")
@Getter
public class Medicine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 20)
  private String unit;

  protected Medicine() {}

  public Medicine(String name, String unit) {
    this.name = name;
    this.unit = unit;
  }
}
