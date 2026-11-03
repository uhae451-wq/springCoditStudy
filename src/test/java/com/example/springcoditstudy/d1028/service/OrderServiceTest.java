package com.example.springcoditstudy.d1028.service;

import com.example.springcoditstudy.d1027.controller.*;
import com.example.springcoditstudy.d1027.domain.*;
import com.example.springcoditstudy.d1027.dto.*;
import com.example.springcoditstudy.d1027.exception.*;
import com.example.springcoditstudy.d1027.repository.*;
import com.example.springcoditstudy.d1027.service.*;

import com.example.springcoditstudy.d1027.domain.Order1027.OrderStatus;
import com.example.springcoditstudy.d1027.domain.Payment1027.PayMethod;
import com.example.springcoditstudy.d1027.exception.MemberNotFoundException;
import com.example.springcoditstudy.d1027.exception.OutOfStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  Member1027Repository memberRepository;
  @Mock
  Book1027Repository bookRepository;
  @Mock
  Order1027Repository orderRepository;
  @Mock
  Payment1027Repository paymentRepository;

  @InjectMocks
  Order1027Service orderService;

  Member1027 member;
  Book1027 book;

  @BeforeEach
  void setUp(){
    // given
    member = new Member1027("김현수","user@naver.com");
    book = new Book1027("공포","김수한",50000,50);
  }

  @Test
  @DisplayName("주문하면 재고가 줄고 결제가 저장된다. 성공케이스")
  void success_01(){
    when(memberRepository.findById(1l)).thenReturn(Optional.of(member));
    when(bookRepository.findById(1l)).thenReturn(Optional.of(book));
    // when
    orderService.order(1l,List.of(new Order1027LineRequest(1l,2)),PayMethod.CARD);
    // then
    assertThat(book.getStock()).isEqualTo(48);
    verify(orderRepository).save(any(Order1027.class));
    verify(paymentRepository).save(any(Payment1027.class));

  }

  @Test
  @DisplayName("없는 회원으로 주문하면 장애가 발생한다.")
  void fail_01(){
    // given
    when(memberRepository.findById(1l)).thenReturn(Optional.empty());
    // when then
    assertThatThrownBy(
        () -> orderService.order(1l , List.of(new Order1027LineRequest(1l,2)),PayMethod.CARD))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @Test
  @DisplayName("재고량 보다 많이 주문이 들어왔을 경우")
  void fail_02(){
    // given
    when(memberRepository.findById(1l)).thenReturn(Optional.of(member));
    when(bookRepository.findById(1l)).thenReturn(Optional.of(book));
    // when
    assertThatThrownBy(()->
        orderService.order(1l, List.of(new Order1027LineRequest(1l,111)),PayMethod.CARD)).isInstanceOf(
        OutOfStockException.class);
    // then
    verify(paymentRepository,never()).save(any());
    assertThat(book.getStock()).isEqualTo(50);
  }

  @Test
  @DisplayName("취소한 재고가 복구 되는지 테스트")
  void cancel_success_case_01(){
    // given
    Order1027 order = Order1027.create(member);
    order.addOrderItem(new OrderItem1027(book,2));
    book.removeStock(2);
    when(orderRepository.findById(1l)).thenReturn(Optional.of(order));
    // when
    orderService.cancel(1l);
    // then
    assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
    assertThat(book.getStock()).isEqualTo(50);
  }

  @Test
  @DisplayName("주문 1개 이상의 책을 주문하는데 총액이 맞는지 확인하는 테스트")
  void order_success_case_02(){
    // given
    // 회원이 한번에 2종류의 책을 구매한다.
    Book1027 book2 = new Book1027("로맨스","김도학",10000,10);
    when(memberRepository.findById(1l)).thenReturn(Optional.of(member));
    when(bookRepository.findById(1l)).thenReturn(Optional.of(book));
    when(bookRepository.findById(2l)).thenReturn(Optional.of(book2));
    // when
    long result = orderService.order(1l,List.of(new Order1027LineRequest(1l,2),new Order1027LineRequest(2l,2)),PayMethod.CARD);
    // then
    assertThat(result).isEqualTo(120000);

  }



}