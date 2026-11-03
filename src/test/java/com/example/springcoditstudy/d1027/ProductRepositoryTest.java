package com.example.springcoditstudy.d1027;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springcoditstudy.d1026.domain.Product1026;
import com.example.springcoditstudy.d1026.repository.Product1026Repository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTest {

  @Autowired
  Product1026Repository product1026Repository;

  // 각 테스트 마다 더미 데이터 세팅
  @BeforeEach
  void setUp(){
    product1026Repository.save("기계식 키보드", 30000, 10);
    product1026Repository.save("무선 키보드", 45000, 3);
    product1026Repository.save("마우스", 20000, 0);
    product1026Repository.save("모니터", 250000, 5);
  }

  @Test
  @DisplayName("재고 5 이하: 경계값 5 도 포함되어 3개")
  void findByStockLessThanEqual() {
    List<Product1026> result = product1026Repository.findByStockLessThanEqual(5);

    assertThat(result)
        .extracting(Product1026::getName)
        .containsExactlyInAnyOrder("무선 키보드", "마우스", "모니터");
  }

  @Test
  @DisplayName("이름에 '키보드' 가 들어간 상품 2개")
  void findByNameContaining() {
    List<Product1026> result = product1026Repository.findByNameContaining("키보드");

    assertThat(result)
        .extracting(Product1026::getName)
        .containsExactlyInAnyOrder("기계식 키보드", "무선 키보드");
  }

  @Test
  @DisplayName("검색결과가 없는 반환")
  void findByNameContaining_empty(){
    List<Product1026> result = product1026Repository.findByNameContaining("스피커");
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("가격 범위 안에 있는지 확인, 싼 가격 정렬")
  void findInPriceRange(){
    List<Product1026> result = product1026Repository.findInPriceRange(20000,45000);
    assertThat(result).
        extracting(Product1026::getName).containsExactlyInAnyOrder("마우스","기계식 키보드","무선 키보드");
  }
}
