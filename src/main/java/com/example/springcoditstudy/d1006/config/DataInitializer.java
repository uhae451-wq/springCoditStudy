package com.example.springcoditstudy.d1006.config;

import com.example.springcoditstudy.d1006.entity.Book1006;
import com.example.springcoditstudy.d1006.entity.Category1006;
import com.example.springcoditstudy.d1006.entity.Member1006;
import com.example.springcoditstudy.d1006.repository.Book1006Repository;
import com.example.springcoditstudy.d1006.repository.Category1006Repository;
import com.example.springcoditstudy.d1006.repository.Member1006Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final Category1006Repository categoryRepository;
  private final Member1006Repository memberRepository;
  private final Book1006Repository bookRepository;

  @Override
  @Transactional
  public void run(String... args) {
    Category1006 novel = categoryRepository.save(new Category1006("소설"));

    for (int i = 1; i <= 5; i++) {
      memberRepository.save(new Member1006("회원" + i, "member" + i + "@test.com"));
    }

    // 페이징(size=5) 확인을 위해 6권 이상 넣는다
    for (int i = 1; i <= 10; i++) {
      Book1006 book = new Book1006("도서 " + i, "저자 " + i, 10000, 5);
      book.assignCategory(novel);
      bookRepository.save(book);
    }
  }
}
