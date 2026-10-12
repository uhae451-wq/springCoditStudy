package com.example.springcoditstudy.d0930;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.time.Period;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 100, unique = true)
  private String email;

  private LocalDate birthDate;

  @Transient
  private int age;

  public Student(String name,String email,LocalDate birthDate){
    this.name = name;
    this.email = email;
    this.birthDate = birthDate;
  }

  public void calculateAge() {
    this.age = Period.between(birthDate, LocalDate.now()).getYears();
  }

}
