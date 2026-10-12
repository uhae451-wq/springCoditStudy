package com.example.springcoditstudy.d1006.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories_d1006")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryD1006 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, length = 30, unique = true)
  private String name;

  public CategoryD1006(String name){
    this.name = name;
  }
}
