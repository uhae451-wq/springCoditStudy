package com.example.springcoditstudy.d0831.bean;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Ivysaur implements BattlePokemon{

    @Override
    public void useSkill() {
        System.out.println("덩쿨채찍 발동! \uD83C\uDF3F");
    }
}