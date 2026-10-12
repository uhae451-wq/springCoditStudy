package com.example.springcoditstudy.d1001.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "doctors")
@Getter
public class Doctor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 20, unique = true, updatable = false)
  private String licenseNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;

  protected Doctor() {}

  public Doctor(String name, String licenseNo, Department department) {
    this.name = name;
    this.licenseNo = licenseNo;
    assignTo(department);
  }

  public void assignTo(Department department) {
    this.department = department;
    department.getDoctors().add(this);
  }


}
