package com.example.springcoditstudy.d1028.domain;

import com.example.springcoditstudy.d1027.controller.*;
import com.example.springcoditstudy.d1027.domain.*;
import com.example.springcoditstudy.d1027.dto.*;
import com.example.springcoditstudy.d1027.exception.*;
import com.example.springcoditstudy.d1027.repository.*;
import com.example.springcoditstudy.d1027.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderTest {

  @Autowired
  Order1027Repository orderRepository;

  @Autowired
  Member1027Repository memberRepository;

  Book1027 book;
  Member1027 member;
  Order1027 order;

  @BeforeEach
  void setUp(){
    book = new Book1027("책제목","김학수",30000,10);
    member = new Member1027("김학수","user@naver.com");

  }

  @Test
  @DisplayName("")
  void 주문_총액은_품목_금액의_합이다(){
    order = Order1027.create(member);
    order.addOrderItem(new OrderItem1027(book,2));

  }
}