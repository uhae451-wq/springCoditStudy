package com.example.springcoditstudy.d1028.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.springcoditstudy.d1027.domain.Member1027;
import com.example.springcoditstudy.d1027.repository.Member1027Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class MemberRepositoryTest {

  @Autowired
  Member1027Repository memberRepository;

  @Test
  @DisplayName("중복 이메일 체크 테스트")
  void duplicate_email_test_success_case_01(){
    memberRepository.saveAndFlush(new Member1027("김현수","user@naver.com"));
    assertThatThrownBy(() ->
        memberRepository.saveAndFlush(new Member1027("회원2", "user@naver.com")))
        .isInstanceOf(DataIntegrityViolationException.class);

  }

}
