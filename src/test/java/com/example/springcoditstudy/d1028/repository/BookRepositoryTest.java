package com.example.springcoditstudy.d1028.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.springcoditstudy.d1027.controller.*;
import com.example.springcoditstudy.d1027.domain.*;
import com.example.springcoditstudy.d1027.dto.*;
import com.example.springcoditstudy.d1027.exception.*;
import com.example.springcoditstudy.d1027.repository.*;
import com.example.springcoditstudy.d1027.service.*;

import com.example.springcoditstudy.d1027.domain.Category1027;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class BookRepositoryTest {

  @Autowired
  Book1027Repository bookRepository;
  @Autowired
  Category1027Repository categoryRepository;


  Category1027 category;

  @Test
  @DisplayName("임의 수량 미만의 재고들 가격으로 내림차순 테스트")
  void test_case_01(){
    // given
    category = new Category1027("로맨스");
    categoryRepository.save(category);
    Book1027 book1 = new Book1027("자바","김학수",3000,20);
    Book1027 book2 = new Book1027("스프링","김도영",30000,10);
    book1.assignCategory(category);
    book2.assignCategory(category);
    bookRepository.save(book1);
    bookRepository.save(book2);

    // when
    List<Book1027> bookList = bookRepository.findLowStockInCategory(30,category.getId());

    // then
    assertThat(bookList.size()).isEqualTo(2);
    assertThat(bookList.get(0).getTitle()).isEqualTo("스프링");
    assertThat(bookList.get(1).getTitle()).isEqualTo("자바");

  }



}