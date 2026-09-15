package com.example.springcoditstudy.d0831.bean;

import org.springframework.stereotype.Component;

@Component
public class Snorlax implements BattlePokemon{

    @Override
    public void useSkill() {
        System.out.println("몸통박치기 발동! \uD83D\uDCA5");
    }
}
