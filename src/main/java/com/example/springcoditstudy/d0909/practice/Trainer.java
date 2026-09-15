package com.example.springcoditstudy.d0909.practice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trainer {
    private Long id;
    private  String name;
    private String email;
    private String region;
}