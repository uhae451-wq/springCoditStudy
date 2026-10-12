package com.example.springcoditstudy.d1001.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "departments")
@Getter
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(nullable = false, length = 30, unique = true)
  private String name;

  @OneToMany(mappedBy = "department", cascade = CascadeType.PERSIST)
  private List<Doctor> doctors = new ArrayList<>();

  protected Department() {}

  public Department(String name) {
    this.name = name;
  }
}