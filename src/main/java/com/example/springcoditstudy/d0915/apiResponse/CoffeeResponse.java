package com.example.springcoditstudy.d0915.apiResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CoffeeResponse {
    private final Long id;
    private final String name;
    private final int price;

    public static CoffeeResponse from(Coffee coffee) {
        return new CoffeeResponse(coffee.getId(), coffee.getName(), coffee.getPrice());
    }

}