package com.example.springcoditstudy.d0915.apiResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private final String code;
    private final String message;
}
