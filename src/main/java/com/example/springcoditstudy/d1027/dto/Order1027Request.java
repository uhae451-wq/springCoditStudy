package com.example.springcoditstudy.d1027.dto;

import com.example.springcoditstudy.d1027.domain.Payment1027;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Order1027Request {

    @NotNull(message = "회원 id는 필수입니다")
    private Long memberId;

    // @Valid 가 없으면 목록 안쪽의 검증이 조용히 건너뛰어집니다
    @NotEmpty(message = "주문 품목은 최소 한 개여야 합니다")
    @Valid
    private List<Order1027LineRequest> lines;

    @NotNull(message = "결제 수단은 필수입니다")
    private Payment1027.PayMethod method;
}
