package com.example.springcoditstudy.d0930;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String title;

  @Column(nullable = false)
  private int price;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CourseLevel level = CourseLevel.BEGINNER;

  @Column(nullable = false,updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  public Course(String title, int price,CourseLevel courseLevel){
    this.title = title;
    this.price = price;
    this.level = courseLevel;
  }

  public void changePrice(int price) { this.price = price; }

  public enum CourseLevel {BEGINNER, INTERMEDIATE, ADVANCED}
}
