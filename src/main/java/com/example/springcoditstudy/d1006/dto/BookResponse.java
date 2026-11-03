package com.example.springcoditstudy.d1006.dto;

import com.example.springcoditstudy.d1006.entity.Book1006;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookResponse {
  private Long id;
  private String title;
  private String author;
  private int price;
  private int stock;

  public static BookResponse from(Book1006 book) {
    return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(),book.getPrice(), book.getStock());
  }

}
