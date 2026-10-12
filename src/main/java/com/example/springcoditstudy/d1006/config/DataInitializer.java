package com.example.springcoditstudy.d1006.config;

import com.example.springcoditstudy.d1006.entity.BookD1006;
import com.example.springcoditstudy.d1006.entity.CategoryD1006;
import com.example.springcoditstudy.d1006.entity.MemberD1006;
import com.example.springcoditstudy.d1006.repository.BookRepository;
import com.example.springcoditstudy.d1006.repository.CategoryRepository;
import com.example.springcoditstudy.d1006.repository.MemberRepositoryD1006;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final CategoryRepository categoryRepository;
  private final MemberRepositoryD1006 memberRepository;
  private final BookRepository bookRepository;

  @Override
  @Transactional
  public void run(String... args) {
    CategoryD1006 novel = categoryRepository.save(new CategoryD1006("소설"));

    for (int i = 1; i <= 5; i++) {
      memberRepository.save(new MemberD1006("회원" + i, "member" + i + "@test.com"));
    }

    // 페이징(size=5) 확인을 위해 6권 이상 넣는다
    for (int i = 1; i <= 10; i++) {
      BookD1006 book = new BookD1006("도서 " + i, "저자 " + i, 10000, 5);
      book.assignCategory(novel);
      bookRepository.save(book);
    }
  }
}
