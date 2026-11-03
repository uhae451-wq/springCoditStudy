package com.example.springcoditstudy.d1027;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springcoditstudy.d1026.domain.Product1026;
import com.example.springcoditstudy.d1026.repository.Member1026Repository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest // Repository 테스트
public class Member1026RepositoryTest {

  @Autowired
  Member1026Repository member1026Repository;

  @Test
  @DisplayName("저장된 이메일이 있다면 true")
  void existsByEmail_true(){
    // given
    member1026Repository.save("김현수", "user@naver.com",22);
    // when
    boolean result = member1026Repository.existsByEmail("user@naver.com");
    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("저장되지 않은 이메일이면 False")
  void existByEmail_false(){
    // given

    // when
    boolean result = member1026Repository.existsByEmail("user@naver.com");
    // then
    assertThat(result).isFalse();
  }

}
