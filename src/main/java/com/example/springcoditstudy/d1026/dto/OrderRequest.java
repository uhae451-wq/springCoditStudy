package com.example.springcoditstudy.d1026.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {

  @NotNull(message = "회원ID는 필수 값 입니다.")
  private Long memberId;

  @NotEmpty(message = "주문 품목은 하나 이상이여야 합니다.")
  private List<OrderLineRequest> lines;
}
