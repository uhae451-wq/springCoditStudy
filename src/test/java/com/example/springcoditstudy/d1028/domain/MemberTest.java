package com.example.springcoditstudy.d1028.domain;

import com.example.springcoditstudy.d1027.controller.*;
import com.example.springcoditstudy.d1027.domain.*;
import com.example.springcoditstudy.d1027.dto.*;
import com.example.springcoditstudy.d1027.exception.*;
import com.example.springcoditstudy.d1027.repository.*;
import com.example.springcoditstudy.d1027.service.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

  //

  @Test
  @DisplayName("회원 등급이 골드 이상이면 주문 금액의 10%를 적립한다.")
  void test(){
    Member1027 newMember= new Member1027("골드회원","user@naver.com");
    newMember.upgradeToGold();

    newMember.earnPoint(50000);

    assertThat(newMember.getPoint()).isEqualTo(5000);


  }

}