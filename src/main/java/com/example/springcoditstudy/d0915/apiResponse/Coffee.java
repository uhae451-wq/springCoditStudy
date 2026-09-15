package com.example.springcoditstudy.d0915.apiResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Coffee {
    private final Long id;
    private final String name;
    private final int price;
    private final boolean inStock;
}
