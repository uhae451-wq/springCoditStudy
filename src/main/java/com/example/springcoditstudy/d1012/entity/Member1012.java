package com.example.springcoditstudy.d1012.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members_d1012")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member1012 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  // ManyToOne은 기본이 EAGER라서 LAZY를 명시해야 한다.
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team1012 team;

  public Member1012(String name, Team1012 team) {
    this.name = name;
    this.team = team;
  }

}
