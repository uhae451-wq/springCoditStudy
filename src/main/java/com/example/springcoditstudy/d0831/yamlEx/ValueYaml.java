package com.example.springcoditstudy.d0831.yamlEx;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ValueYaml {

    @Value("${my.greeting}")
    private String greeting;

}
