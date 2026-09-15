package com.example.springcoditstudy.d0909.practice;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainerRepository {

    private final Map<Long,Trainer> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Trainer save(String name, String email, String region){
        Long id = idGenerator.getAndIncrement(); // 순차적 증가
        Trainer trainer = new Trainer(id,name,email,region);
        store.put(id,trainer);
        return trainer;
    }

    public Trainer findById(Long id){
        return store.get(id);
    }

    public List<Trainer> findAll() {
        return new ArrayList<>(store.values());
    }
}