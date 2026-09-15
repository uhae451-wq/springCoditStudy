package com.example.springcoditstudy.d0831;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokemonService {

    private final Pokemon primaryPokemon;
    private final Pokemon qualifiedPokemon;
    private final List<Pokemon> allPokemon;

    public PokemonService(Pokemon primaryPokemon, @Qualifier("squirtle")Pokemon qualifiedPokemon, List<Pokemon> allPokemon) {
        this.primaryPokemon = primaryPokemon;
        this.qualifiedPokemon = qualifiedPokemon;
        this.allPokemon = allPokemon;
    }
    public void battleStart() {
        System.out.println("=== 1. 아무 지정 없이 주입받은 Pokemon (@Primary가 선택됨) ===");
        primaryPokemon.attack();

        System.out.println("=== 2. @Qualifier(\"squirtle\")로 콕 집어 주입받은 Pokemon ===");
        qualifiedPokemon.attack();

        System.out.println("=== 3. List<Pokemon>으로 전체 주입받아 순서대로 공격 ===");
        allPokemon.forEach(Pokemon::attack);
    }

}

