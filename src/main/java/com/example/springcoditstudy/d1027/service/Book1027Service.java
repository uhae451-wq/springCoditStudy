package com.example.springcoditstudy.d1027.service;

import com.example.springcoditstudy.d1027.domain.Category1027;
import com.example.springcoditstudy.d1027.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springcoditstudy.d1027.domain.Book1027;
import com.example.springcoditstudy.d1027.repository.Book1027Repository;
import com.example.springcoditstudy.d1027.dto.Book1027Request;
import com.example.springcoditstudy.d1027.dto.Book1027Response;
import com.example.springcoditstudy.d1027.repository.Category1027Repository;

@Service
@RequiredArgsConstructor
public class Book1027Service {

    private final Book1027Repository bookRepository;
    private final Category1027Repository categoryRepository;

    @Transactional
    public Long create(Book1027Request request) {
        Book1027 book = new Book1027(request.getTitle(), request.getAuthor(), request.getPrice(), request.getStock());
        if (request.getCategoryId() != null) {
            Category1027 category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));
            book.assignCategory(category);
        }
        return bookRepository.save(book).getId();
    }

    @Transactional(readOnly = true)
    public Page<Book1027Response> getBooks(Long categoryId, Pageable pageable) {
        Page<Book1027> page = (categoryId == null)
                ? bookRepository.findAll(pageable)
                : bookRepository.findByCategoryId(categoryId, pageable);
        return page.map(Book1027Response::from);
    }
}
