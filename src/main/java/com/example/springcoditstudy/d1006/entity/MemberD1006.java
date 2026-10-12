package com.example.springcoditstudy.d1006.entity;

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
@Table(name = "members_d1006")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberD1006 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 100, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private MemberGrade grade = MemberGrade.NORMAL;

  @Column(nullable = false)
  private int point;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  public MemberD1006(String name, String email){
    this.name = name;
    this.email = email;
  }

  public enum MemberGrade{
    NORMAL, GOLD
  }

}
