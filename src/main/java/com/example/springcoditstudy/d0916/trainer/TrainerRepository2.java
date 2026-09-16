package com.example.springcoditstudy.d0916.trainer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainerRepository2 {

    private final Map<Long, Trainer> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public TrainerResponse save(String name,String specialty,String profileImageFileName){
        // 순차적 증가
        long id = idGenerator.getAndIncrement();
        Trainer trainer = new Trainer(id,name,specialty,profileImageFileName);
        TrainerResponse trainerResponse = new TrainerResponse(trainer.getId(),trainer.getName(),trainer.getSpecialty(),trainer.getProfileImageFileName());
        store.put(id,trainer);
        return trainerResponse;
    }

    public Trainer findById(long id){
        return store.get(id);
    }

    public List<Trainer> findAll(){
        return new ArrayList<Trainer>(store.values());
    }

}
