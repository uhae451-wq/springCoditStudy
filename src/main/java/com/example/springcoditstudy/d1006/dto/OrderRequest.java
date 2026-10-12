package com.example.springcoditstudy.d1006.dto;

import com.example.springcoditstudy.d1006.entity.PaymentD1006.PayMethod;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {

  private Long memberId;
  private List<OrderLineRequest> lines;
  private PayMethod method;
}
