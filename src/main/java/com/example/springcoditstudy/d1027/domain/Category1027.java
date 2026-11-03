package com.example.springcoditstudy.d1027.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories_d1027")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category1027 {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String name;

    public Category1027(String name) {
        this.name = name;
    }
}
