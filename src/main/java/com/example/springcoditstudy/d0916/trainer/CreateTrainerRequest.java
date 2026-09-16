package com.example.springcoditstudy.d0916.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateTrainerRequest {

    @NotNull
    @Schema(description = "트레이너 이름", example = "피카츄")
    private String name;

    @NotBlank
    @Schema(description = "전문 분야", example = "앞구르기")
    private String specialty;

}
