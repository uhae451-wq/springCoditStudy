package com.example.springcoditstudy.d0909.practice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;

    public Trainer createTrainer(String name, String email, String region){
        return trainerRepository.save(name,email,region);
    }

    public Trainer getTrainer(Long id){
        return trainerRepository.findById(id);
    }


    public List<Trainer> getAllTrainer() {
        return trainerRepository.findAll();
    }
}

