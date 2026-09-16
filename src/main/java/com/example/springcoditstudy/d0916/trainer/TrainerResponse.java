package com.example.springcoditstudy.d0916.trainer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrainerResponse {
    private Long id;
    private String name;
    private String specialty;
    private String profileImageUrl;

    public static TrainerResponse form(Trainer trainer){
        String url = trainer.getProfileImageFileName() == null ? null : "/v1/trainers/" + trainer.getId() + "/profile-image";
        return new TrainerResponse(trainer.getId(),trainer.getName(),trainer.getSpecialty(),url);
    }
}
