package com.example.springcoditstudy.d1027.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import com.example.springcoditstudy.d1027.dto.Book1027Request;
import com.example.springcoditstudy.d1027.dto.Book1027Response;
import com.example.springcoditstudy.d1027.service.Book1027Service;

@RestController
@RequestMapping("/d1027/v1/books")
@RequiredArgsConstructor
public class Book1027Controller {

    private final Book1027Service bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Long> create(@Valid @RequestBody Book1027Request request) {
        return Map.of("id", bookService.create(request));
    }

    @GetMapping
    public Page<Book1027Response> list(@RequestParam(required = false) Long categoryId,
                                   @PageableDefault(sort = "id") Pageable pageable) {
        return bookService.getBooks(categoryId, pageable);
    }
}
