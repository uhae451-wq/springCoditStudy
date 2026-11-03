package com.example.springcoditstudy.d1027.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "members_d1027")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member1027 {

    private static final double goldPointRate = 0.1;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Member1027(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void addPoint(int amount) { this.point += amount; }

  public void upgradeToGold() {
    this.grade = MemberGrade.GOLD;
  }

    public void earnPoint(int orderMount) {
        if(isGold()){
            this.point += orderMount*goldPointRate;
        }
    }

    private boolean isGold(){
        return this.grade == MemberGrade.GOLD;
    }

    public enum MemberGrade { NORMAL, GOLD }
}
