package com.example.springcoditstudy.d1026.repository;

import com.example.springcoditstudy.d1002.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j
@Component("member")
@RequiredArgsConstructor
public class MemberHealthIndicator implements HealthIndicator {

  private final Member1026Repository member1026Repository;


  @Override
  public Health health() {
    try {
      long count = member1026Repository.count();
      return Health.up()
          .withDetail("memberCount", count)
          .withDetail("storage", "in-memory")
          .build();

    } catch (Exception e) {
      log.error("회원 저장소 상태를 확인하지 못했습니다", e);
      return Health.down()
          .withDetail("reason", "회원 저장소에 접근할 수 없습니다")
          .build();
    }
  }
}