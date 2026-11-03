package com.example.springcoditstudy.d1027.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.example.springcoditstudy.d1027.domain.Book1027;

@Getter
@AllArgsConstructor
public class Book1027Response {

    private Long id;
    private String title;
    private String author;
    private int price;
    private int stock;

    public static Book1027Response from(Book1027 book) {
        return new Book1027Response(book.getId(), book.getTitle(), book.getAuthor(),
                book.getPrice(), book.getStock());
    }
}
