package com.example.springcoditstudy.d1012.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// N+1 실습용 엔티티 (팀 1 : 멤버 N)
@Entity
@Table(name = "teams_d1012")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team1012 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  // OneToMany는 기본이 LAZY. members는 DB 컬럼이 아니라 member 테이블의 team_id로 찾아오는 자바 쪽 리스트
  @OneToMany(mappedBy = "team")
  private List<Member1012> members = new ArrayList<>();

  public Team1012(String name) {
    this.name = name;
  }

}
