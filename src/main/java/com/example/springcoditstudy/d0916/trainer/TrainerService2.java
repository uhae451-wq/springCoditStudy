package com.example.springcoditstudy.d0916.trainer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService2 {

    private final TrainerRepository2 trainerRepository;

    public TrainerResponse createTrainer(String name, String specialty,String profileName){
        TrainerResponse trainer = trainerRepository.save(name,specialty,profileName);
        return trainer;
    }

    public Trainer getTrainer(Long id){
        Trainer trainer = trainerRepository.findById(id);
        if(trainer == null) throw new TrainerNotFoundException(id);
        return trainer;
    }

    public List<Trainer> getTrainers(String specialty){
        List<Trainer> allTrainer = trainerRepository.findAll();
        if(specialty == null) return allTrainer;
        return allTrainer.stream()
                .filter(e -> e.getSpecialty().equals(specialty))
                .toList();
    }

}
