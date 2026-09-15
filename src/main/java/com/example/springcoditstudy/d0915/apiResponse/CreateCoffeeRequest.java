package com.example.springcoditstudy.d0915.apiResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCoffeeRequest {

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @Positive(message = "가격은 0보다 커야합니다.")
    private int price;
}