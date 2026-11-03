package com.example.springcoditstudy.d1006.service;

import com.example.springcoditstudy.d1006.dto.BookRequest;
import com.example.springcoditstudy.d1006.dto.BookResponse;
import com.example.springcoditstudy.d1006.entity.Book1006;
import com.example.springcoditstudy.d1006.entity.Category1006;
import com.example.springcoditstudy.d1006.repository.Book1006Repository;
import com.example.springcoditstudy.d1006.repository.Category1006Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Book1006Service {
  private final Book1006Repository bookRepository;
  private final Category1006Repository categoryRepository;

  @Transactional
  public Long create(BookRequest request) {
    Book1006 book = new Book1006(request.getTitle(), request.getAuthor(), request.getPrice(), request.getStock());
    if (request.getCategoryId() != null) {
      Category1006 category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> new IllegalArgumentException("분류를 찾을 수 없습니다: " + request.getCategoryId()));
      book.assignCategory(category);
    }
    return bookRepository.save(book).getId();
  }

  @Transactional(readOnly = true)
  public Page<BookResponse> getBooks(Long categoryId, Pageable pageable) {
    Page<Book1006> page = (categoryId == null) ? bookRepository.findAll(pageable) : bookRepository.findByCategoryId(categoryId, pageable);
    return page.map(BookResponse::from);
  }
}
