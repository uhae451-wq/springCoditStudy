package com.example.springcoditstudy.d1006.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderLineRequest {

  private Long bookId;
  private int quantity;
}
