package com.example.springcoditstudy.d0916.trainer;

public class TrainerNotFoundException extends RuntimeException {
    public TrainerNotFoundException(Long id) {
        super("해당 트레이너를 찾을 수 없습니다. " + id);
    }
}
