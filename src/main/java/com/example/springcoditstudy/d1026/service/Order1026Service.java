package com.example.springcoditstudy.d1026.service;

import com.example.springcoditstudy.d0824.Member;
import com.example.springcoditstudy.d1026.domain.Member1026;
import com.example.springcoditstudy.d1026.domain.Product1026;
import com.example.springcoditstudy.d1026.dto.OrderLineRequest;
import com.example.springcoditstudy.d1026.exception.ErrorCode;
import com.example.springcoditstudy.d1026.exception.MemberNotFoundException;
import com.example.springcoditstudy.d1026.exception.ProductNotFoundException;
import com.example.springcoditstudy.d1026.repository.Member1026Repository;
import com.example.springcoditstudy.d1026.repository.Product1026Repository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Order1026Service {

  private final Member1026Repository memberRepository;
  private final Product1026Repository productRepository;

  public String order(Long memberId, List<OrderLineRequest> lines) {

    log.info("주문 요청 멤버ID = {},  품목수 = {} ",memberId,lines.size());
    Member1026 member = memberRepository.findById(memberId)
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

    int total = 0;
    for(OrderLineRequest line : lines){
      Product1026 product = productRepository.findById(line.getProductId())
          .orElseThrow(() -> new ProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
      log.info("재고확인 ProductID = {}, 현재재고 = {}, 요청 = {} ",product.getId(),product.getStock(),line.getQuantity());

      product.removeStock(line.getQuantity());
      total += product.getPrice() * line.getQuantity();

    }

    log.info("주문 완료 memberID = {}, 품목수 = {} , 총액 = {} ",member,lines.size(),total);


    return member.getName() + "님이 " + lines.size() + "개를 주문했습니다. 총 주문 금액은 " + total + "원 입니다.";
  }
}
