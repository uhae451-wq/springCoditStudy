package com.example.springcoditstudy.d1028.service;

import com.example.springcoditstudy.d1026.service.Order1026Service;
import com.example.springcoditstudy.d1027.controller.*;
import com.example.springcoditstudy.d1027.domain.*;
import com.example.springcoditstudy.d1027.dto.*;
import com.example.springcoditstudy.d1027.exception.*;
import com.example.springcoditstudy.d1027.repository.*;
import com.example.springcoditstudy.d1027.service.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.springcoditstudy.d1027.domain.Payment1027.PayMethod;
import com.example.springcoditstudy.d1027.exception.OutOfStockException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceIntergrationTest {

  @Autowired
  Order1027Service orderService;
  @Autowired
  Member1027Repository memberRepository;
  @Autowired
  Book1027Repository bookRepository;
  @Autowired
  Payment1027Repository paymentRepository;
  @Autowired
  Order1027Repository orderRepository;

  Long memberId;
  Long bookId;
  Long book2Id;

  @AfterEach
  void tearDown(){
    // 자식(결제 → 주문)부터 지워야 회원/책 삭제 때 FK 위반이 나지 않는다
    paymentRepository.deleteAll();
    orderRepository.deleteAll();
    memberRepository.deleteById(memberId);
    bookRepository.deleteById(bookId);
    bookRepository.deleteById(book2Id);
  }

  @Test
  @DisplayName("정상 주문이 되면 재고가 줄어든다")
  void test01(){
    memberId = memberRepository.save(new Member1027("김학수","user@naver.com")).getId();
    bookId = bookRepository.save(new Book1027("자바의 정석","김동수",30000,5)).getId();
    book2Id = bookRepository.save(new Book1027("코드잇","코더",20000,5)).getId();

    orderService.order(memberId,List.of(new Order1027LineRequest(bookId,2)),PayMethod.CARD);

    assertThat(bookRepository.findById(bookId).get().getStock()).isEqualTo(3);
  }

  @Test
  @DisplayName("재고가 부족하면 주문과 결제가 전부 롤백 된다.")
  void test(){
    // given
    memberId = memberRepository.save(new Member1027("김학수","user@naver.com")).getId();
    bookId = bookRepository.save(new Book1027("자바의 정석","김동수",30000,5)).getId();
    book2Id = bookRepository.save(new Book1027("코드잇","코더",20000,5)).getId();

    // when
    assertThatThrownBy(
        () ->  orderService.order(memberId,
            List.of(new Order1027LineRequest(bookId,2),new Order1027LineRequest(book2Id,99)),
            PayMethod.CARD)
    ).isInstanceOf(OutOfStockException.class)
   ;
    // then
    assertThat(bookRepository.findById(bookId).get().getTitle()).isEqualTo("자바의 정석");
  }

  @Test
  @DisplayName("취소하면 save없이 바로 재고가 돌아온다.")
  void test02(){
    memberId =  memberRepository.save(new Member1027("김학수","user@naver.com")).getId();
    bookId = bookRepository.save(new Book1027("자바의정석","김동하",3000,3)).getId();
    book2Id = bookRepository.save(new Book1027("자바의정석1","김동하1",3000,3)).getId();
    Long orderId = orderService.order(memberId,List.of(new Order1027LineRequest(bookId,2)),PayMethod.CARD);
    orderService.cancel(orderId);

    assertThat(bookRepository.findById(bookId).get().getStock()).isEqualTo(3);

  }


}