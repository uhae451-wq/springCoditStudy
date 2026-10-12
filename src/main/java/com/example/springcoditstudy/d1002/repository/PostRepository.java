package com.example.springcoditstudy.d1002.repository;

import com.example.springcoditstudy.d1002.Entity.Member;
import com.example.springcoditstudy.d1002.Entity.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

  // 제목에 특정 키워드가 포함된 게시글 리스트
  List<Post> findByTitleContaining(String keyword);

  @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword%")
  List<Post> searchByTitle(@Param("keyword") String keyword);

  List<Post> findByAuthor(Member author);

  /*Page<Post> findByTitle(String string, Pageable pageable); */

  Slice<Post> findByTitle(String string,Pageable pageable);
}
