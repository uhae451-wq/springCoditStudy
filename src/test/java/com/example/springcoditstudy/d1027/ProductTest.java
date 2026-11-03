package com.example.springcoditstudy.d1027;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.springcoditstudy.d1026.domain.Product1026;
import com.example.springcoditstudy.d1026.exception.OutOfStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductTest {

  private Product1026 product;

  @BeforeEach
  void setUp() {
    product = new Product1026(1L, "키보드", 30000, 10);   // 재고 10개
  }

  @Test
  void 재고보다_적게_주문하면_재고가_줄어든다_성공테스트(){

    // given

    // when
    product.removeStock(5);
    // then
    assertEquals(5,product.getStock());
  }

  @Test
  void 내가_가지고있는_수량보다_많은_주문이_들어온_경우(){

    // given

    // when / then ( when이 발생하면 예외가 발생하므로 같이 작성 )
    assertThrows(OutOfStockException.class, () -> product.removeStock(11));
  }

  @Test
  void 경계값_테스트(){
    // given

    // when
    product.removeStock(10);
    // then
    assertEquals(0,product.getStock());
  }

/*  @Test
  @DisplayName("음수 수량으로 주문해도 재고는 그대로여야 합니다")
  void removeStock_negative() {
    try {
      product.removeStock(-5);
    } catch (RuntimeException ignored) {
    }

    assertEquals(10, product.getStock());
  }*/
}
