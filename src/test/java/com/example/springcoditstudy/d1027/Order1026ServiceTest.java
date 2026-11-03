package com.example.springcoditstudy.d1027;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springcoditstudy.d1026.domain.Member1026;
import com.example.springcoditstudy.d1026.domain.Product1026;
import com.example.springcoditstudy.d1026.dto.OrderLineRequest;
import com.example.springcoditstudy.d1026.exception.MemberNotFoundException;
import com.example.springcoditstudy.d1026.exception.OutOfStockException;
import com.example.springcoditstudy.d1026.exception.ProductNotFoundException;
import com.example.springcoditstudy.d1026.repository.Member1026Repository;
import com.example.springcoditstudy.d1026.repository.Product1026Repository;
import com.example.springcoditstudy.d1026.service.Order1026Service;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class Order1026ServiceTest {

  // OrderService를 사용하기 위해 Repository가 필요한데 가짜 mok 객체를 통해서 만들어준다.
  @Mock
  private Member1026Repository member1026Repository;
  @Mock
  private Product1026Repository product1026Repository;

  // 주입할 서비스대상(메인) -> 위에 가짜 대상을 여기에 넣는다.
  @InjectMocks
  private Order1026Service order1026Service;

  @Test
  void 정상_주문(){
    // given
    // domain 생성
    Member1026 member1026 = new Member1026(1l,"김현수","22@naver.com",22);
    Product1026 product1026 = new Product1026(1l,"키보드",3000,10);

    when(member1026Repository.findById(1l)).thenReturn(Optional.of(member1026));
    when(product1026Repository.findById(1l)).thenReturn(Optional.of(product1026));

    // when
    String result = order1026Service.order(1l, List.of(new OrderLineRequest(1l,2)));

    // then
    // "김현수님이 2개를 주문했습니다. 총 주문 금액은 6000원 입니다."
    assertEquals("김현수님이 1개를 주문했습니다. 총 주문 금액은 6000원 입니다.",result);
  }

  @Test
  @DisplayName("없는 회원으로 주문을 넣을 경우")
  void 주문_실패(){
    // given
    when(member1026Repository.findById(99l)).thenReturn(Optional.empty());
    // when
    assertThrows(MemberNotFoundException.class,
        () -> order1026Service.order(99l,List.of(new OrderLineRequest(1l,2))));
    // then
    // product1026Repository에 findById가 실행X 검증
    verify(product1026Repository, never()).findById(any());
    verify(member1026Repository, times(1)).findById(any());
  }

  @Test
  @DisplayName("없는 상품이라면 ProductNotFoundException이 발생하는지 확인")
  void order_product_not_found(){
    // given
    when(member1026Repository.findById(2l)).thenReturn(Optional.of(member));
    when(product1026Repository.findById(999l)).thenReturn(Optional.empty());
    // when
    // then
    assertThrows(ProductNotFoundException.class,() -> order1026Service.order(2l,List.of(new OrderLineRequest(999l,2))));
  }

  @Test
  void order_outStocks(){
    when(member1026Repository.findById(2l)).thenReturn(Optional.of(member));
    when(product1026Repository.findById(2l)).thenReturn(Optional.of(product));
    assertThrows(OutOfStockException.class, ()-> order1026Service.order(2l,List.of(new OrderLineRequest(2l,20))));
  }

  @Test
  @DisplayName("한번에 주문으로 여러개의 주문을 구매하는 경우")
  void order_multipleLines(){
    // given
    Product1026 product1026  = new Product1026(3l,"마우스",2000,5);
    when(member1026Repository.findById(2l)).thenReturn(Optional.of(member));
    when(product1026Repository.findById(2l)).thenReturn(Optional.of(product));
    when(product1026Repository.findById(3l)).thenReturn(Optional.of(product1026));
    // when
    String result = order1026Service.order(2l,
        List.of(
            new OrderLineRequest(2l, 4), // 12
            new OrderLineRequest(3l,5)
        ));
    // then
    assertEquals("김현수님이 2개를 주문했습니다. 총 주문 금액은 22000원 입니다.",result);
  }

  @Test
  @DisplayName("SPY - 진짜 Product를 쓰면서 호출한다.")
  void order_withSpy(){
    // given
    Product1026 product1026 = spy(new Product1026(1l,"키보드",3000,20));
    when(product1026Repository.findById(1l)).thenReturn(Optional.of(product1026));
    when(member1026Repository.findById(2l)).thenReturn(Optional.of(member));

    String result = order1026Service.order(2l,List.of(new OrderLineRequest(1l,2)));

    verify(product1026).removeStock(2);
    assertEquals(18,product1026.getStock());

  }

  private final Member1026 member = new Member1026(2l,"김현수","33@naver.com",33);
  private final Product1026 product = new Product1026(2l,"키보드",3000,10);



}