package com.example.springcoditstudy.d0831.yamlEx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdDataSourceConfig {

    @Bean
    public DataSourceInfoPrinter dataSourceInfoPrinter() {
        return () -> System.out.println("운영용 데이터소스 적용됨");
    }
}
