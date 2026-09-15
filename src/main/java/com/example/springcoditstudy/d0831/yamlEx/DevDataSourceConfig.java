package com.example.springcoditstudy.d0831.yamlEx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevDataSourceConfig {

    @Bean
    public DataSourceInfoPrinter dataSourceInfoPrinter() {
        return () -> System.out.println("개발용 데이터소스 적용됨");
    }
}