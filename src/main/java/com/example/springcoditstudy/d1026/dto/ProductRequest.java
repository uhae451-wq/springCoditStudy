package com.example.springcoditstudy.d1026.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductRequest {

  @BannedWord
  @NotBlank(message = "상품명은 필수 값 입니다.₩")
  private String name;

  @NotNull
  private int price;

  @Min(1)
  private int stock;

  @Phone
  private String contactPhone;
}
