package com.example.springcoditstudy.d0831;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Charmander implements Pokemon{

    @Override
    public void attack() {
        System.out.println("꼬부기 물총 공격💧");
    }

}
