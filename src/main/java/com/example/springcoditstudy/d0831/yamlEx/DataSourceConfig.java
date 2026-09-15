package com.example.springcoditstudy.d0831.yamlEx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSourceInfoPrinter dataSourceInfoPrinter() {
        return () -> System.out.println("기본용 데이터소스 적용됨");
    }
}

