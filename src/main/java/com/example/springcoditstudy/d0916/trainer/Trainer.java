package com.example.springcoditstudy.d0916.trainer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Trainer {
    private long id;
    private String name;
    private String specialty;
    private String profileImageFileName;
}
