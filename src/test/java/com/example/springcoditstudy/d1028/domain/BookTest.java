package com.example.springcoditstudy.d1028.domain;

import com.example.springcoditstudy.d1027.controller.*;
import com.example.springcoditstudy.d1027.domain.*;
import com.example.springcoditstudy.d1027.dto.*;
import com.example.springcoditstudy.d1027.exception.*;
import com.example.springcoditstudy.d1027.repository.*;
import com.example.springcoditstudy.d1027.service.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.springcoditstudy.d1027.domain.Payment1027.PayMethod;
import com.example.springcoditstudy.d1027.exception.InvalidQuantityException;
import com.example.springcoditstudy.d1027.exception.OutOfStockException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookTest {

  @Autowired
  private Order1027Service orderService;

  @Autowired
      private Member1027Repository memberRepository;

  Member1027 member;
  Book1027 book1;
  Book1027 book2;
  @Autowired
  private Book1027Repository bookRepository;

  @BeforeEach
  void setUp(){
    member = new Member1027("김학수","user@naver.com");
    book1 = new Book1027("자바의정석","김영한",20000,10);
    book2 = new Book1027("토이랜드","김토이",30000,20);
  }

  @Test
  @DisplayName("주문하면 재고가 줄어든다")
  void 주문하면_재고가_줄어든다(){
    book1.removeStock(5);
    assertThat(book1.getStock()).isEqualTo(5);
  }

  @Test
  void 재와_같은_수량으로_주문할_수_있다(){
    book1.removeStock(10);
    assertThat(book1.getStock()).isEqualTo(0);
  }

  @Test
  void 재고보다_많이_주문하면_예외가_터진다(){
    assertThatThrownBy(()->book1.removeStock(1000)).isInstanceOf(OutOfStockException.class);
  }

  @Test
  void 음수_수량으로는_차감할_수_없다(){
    assertThatThrownBy(()->book1.removeStock(-1)).isInstanceOf(InvalidQuantityException.class);
  }

  @Test
  @org.springframework.transaction.annotation.Transactional
  void 취소하면_재고가_복구된다(){
    // given
    memberRepository.save(member);
    bookRepository.save(book1);

    Long orderId = orderService.order(member.getId(),
        List.of(
            new Order1027LineRequest(book1.getId(), 2)
        ), PayMethod.CARD);
    orderService.cancel(orderId);
    assertThat(book1.getStock()).isEqualTo(10);
  }

}