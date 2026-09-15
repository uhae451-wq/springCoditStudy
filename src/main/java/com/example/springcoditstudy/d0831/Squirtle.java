package com.example.springcoditstudy.d0831;

import org.springframework.stereotype.Component;

@Component
public class Squirtle implements Pokemon{

    @Override
    public void attack() {
        System.out.println("파이리 불꽃 공격🔥");
    }

}
