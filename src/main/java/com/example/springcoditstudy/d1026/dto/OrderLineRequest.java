package com.example.springcoditstudy.d1026.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderLineRequest {

  @NotNull(message = "상품 ID는 필수 값 입니다.")
  @Valid
  private Long productId;

  @Min(value = 1, message = "수량은 최소 1개 이상이여야 합니다.")
  @Valid
  private int quantity;

}
