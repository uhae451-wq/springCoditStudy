package com.example.springcoditstudy.d1006.controller;

import com.example.springcoditstudy.d1006.dto.BookRequest;
import com.example.springcoditstudy.d1006.dto.BookResponse;
import com.example.springcoditstudy.d1006.service.BookService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String, Long> create(@RequestBody BookRequest request) {
    return Map.of("id", bookService.create(request));
  }

  @GetMapping
  public Page<BookResponse> list(@RequestParam(required = false) Long categoryId,@PageableDefault(sort = "id") Pageable pageable) {
    return bookService.getBooks(categoryId, pageable);
  }
}
