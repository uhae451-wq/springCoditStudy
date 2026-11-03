package com.example.springcoditstudy.d1027;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.springcoditstudy.d1026.domain.Member1026;
import com.example.springcoditstudy.d1026.exception.DuplicatEmailException;
import com.example.springcoditstudy.d1026.repository.Member1026Repository;
import com.example.springcoditstudy.d1026.service.Member1026Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class Member1026ServiceTest {

  @Mock
  private Member1026Repository member1026Repository;

  @InjectMocks
  private Member1026Service member1026Service;

  @Test
  @DisplayName("성공적으로 가입하면 회원정보를 돌려줌")
  void join_success(){
    // given
    Member1026 save = new Member1026(1l,"김현수","user@nave.com",31);
    when(member1026Repository.existsByEmail("user@naver.com")).thenReturn(false);
    when(member1026Repository.save("김현수","user@naver.com",31)).thenReturn(save);
    // when
    Member1026 saveMember = member1026Service.join("김현수","user@naver.com",31);
    // then
    assertEquals(1l,saveMember.getId());
    assertEquals(save.getName(),saveMember.getName());
    assertEquals(save.getEmail(),saveMember.getEmail());
  }

  @Test
  @DisplayName("중복된 이메일 검사")
  void email_duplicate(){
    // given
    when(member1026Repository.existsByEmail("user@naver.com")).thenReturn(true);
    // when & then
    assertThrows(DuplicatEmailException.class, ()->member1026Service.join("김현수","user@naver.com",31));

  }
}
