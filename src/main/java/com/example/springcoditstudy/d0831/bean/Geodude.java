package com.example.springcoditstudy.d0831.bean;

import org.springframework.stereotype.Component;

@Component
public class Geodude implements BattlePokemon{
    @Override
    public void useSkill() {
        System.out.println("뺘-아앙! \uD83D\uDCA5");
    }
}
