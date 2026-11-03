package com.example.springcoditstudy.d1027.config;

import com.example.springcoditstudy.d1027.domain.Book1027;
import com.example.springcoditstudy.d1027.domain.Category1027;
import com.example.springcoditstudy.d1027.domain.Member1027;
import com.example.springcoditstudy.d1027.repository.Book1027Repository;
import com.example.springcoditstudy.d1027.repository.Category1027Repository;
import com.example.springcoditstudy.d1027.repository.Member1027Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// ddl-auto: create 라서 앱을 다시 띄울 때마다 DB 가 비워진다 — 시드 데이터를 넣어준다
@Component
@RequiredArgsConstructor
public class DataInitializer1027 implements CommandLineRunner {

    private final Category1027Repository categoryRepository;
    private final Member1027Repository memberRepository;
    private final Book1027Repository bookRepository;

    @Override
    @Transactional
    public void run(String... args) {
        Category1027 novel = categoryRepository.save(new Category1027("소설"));

        for (int i = 1; i <= 5; i++) {
            memberRepository.save(new Member1027("회원" + i, "member" + i + "@test.com"));
        }

        // 페이징(size=5) 확인을 위해 6권 이상 넣는다
        for (int i = 1; i <= 10; i++) {
            Book1027 book = new Book1027("도서 " + i, "저자 " + i, 10000, 5);
            book.assignCategory(novel);
            bookRepository.save(book);
        }
    }
}
