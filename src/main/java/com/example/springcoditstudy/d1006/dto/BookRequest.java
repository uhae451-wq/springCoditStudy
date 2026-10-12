package com.example.springcoditstudy.d1006.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookRequest {
  private String title;
  private String author;
  private int price;
  private int stock;
  private Long categoryId;
}
