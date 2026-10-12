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
@Table(name = "enrollments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrollment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime enrolledAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EnrollmentStatus status = EnrollmentStatus.ENROLLED;

  @Column(nullable = false)
  private int progressRate;

  public Enrollment(LocalDateTime enrolledAt,EnrollmentStatus enrollmentStatus,int progressRate){
    this.enrolledAt = enrolledAt;
    this.status = enrollmentStatus;
    this.progressRate = progressRate;
  }

  public void updateProgress(int rate) {
    this.progressRate = rate;
    if (rate >= 100) {
      this.status = EnrollmentStatus.COMPLETED;
    }
  }

  public enum EnrollmentStatus{ENROLLED, COMPLETED, CANCELED}

}
