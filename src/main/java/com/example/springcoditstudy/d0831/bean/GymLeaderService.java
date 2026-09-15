package com.example.springcoditstudy.d0831.bean;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GymLeaderService {

    private final BattlePokemon battlePokemon;
    private final List<BattlePokemon> Pokemon;
    private final BattlePokemon snorlax;

    public GymLeaderService(BattlePokemon battlePokemon, @Qualifier("snorlax")BattlePokemon snorlax, List<BattlePokemon> Pokemon){
        this.battlePokemon = battlePokemon;
        this.snorlax = snorlax;
        this.Pokemon = Pokemon;
    }

    public void openGymBattle(){
        battlePokemon.useSkill();
        snorlax.useSkill();
        Pokemon.forEach(BattlePokemon::useSkill);
    }

}
