package com.example.springcoditstudy;

import com.example.springcoditstudy.d0831.PokemonService;
import com.example.springcoditstudy.d0831.yamlEx.DataSourceInfoPrinter;
import com.example.springcoditstudy.d0831.yamlEx.ValueYaml;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringCoditStudyApplication {

    public static void main(String[] args) {
        // SpringApplication.run(SpringCoditStudyApplication.class, args);
        ApplicationContext context = SpringApplication.run(SpringCoditStudyApplication.class, args);

        // d0831 S
        PokemonService pokemonService = context.getBean(PokemonService.class);
        pokemonService.battleStart();

        ValueYaml valueYaml = context.getBean(ValueYaml.class);
        System.out.println(valueYaml.getGreeting());
        DataSourceInfoPrinter printer = context.getBean(DataSourceInfoPrinter.class);
        printer.print();
        // d0831 E
    }

}
