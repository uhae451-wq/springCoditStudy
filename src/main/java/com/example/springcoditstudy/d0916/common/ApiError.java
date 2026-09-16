package com.example.springcoditstudy.d0916.common;

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
