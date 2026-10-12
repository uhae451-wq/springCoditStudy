package com.example.springcoditstudy.d1002;

import com.example.springcoditstudy.d1002.Entity.Comment;
import com.example.springcoditstudy.d1002.Entity.Member;
import com.example.springcoditstudy.d1002.Entity.Post;
import com.example.springcoditstudy.d1002.repository.MemberRepository;
import com.example.springcoditstudy.d1002.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Rollback(value = false)
@Transactional
public class MemberTest {

  @Autowired
  MemberRepository memberRepository;
  @Autowired
  PostRepository postRepository;

  @Test
  void 멤버_게시글_추가(){
    Member member = memberRepository.save(new Member("viva", "viva@mail.com"));

    for (int i = 1; i <= 50; i++) {
      Post post = new Post("제목 " + i, "내용 " + i, member);
      postRepository.save(post);
    }
  }

  @Test
  void 게시글_페이징(){
    Pageable pageable = PageRequest.of(1,10, Sort.by("title"));
    // Page<Post> postsPage = postRepository.findByTitle("제목",pageable);
    Slice<Post> postSlice = postRepository.findByTitle("제목",pageable);
    postSlice.getContent();  // 이번 페이지의 10건
    postSlice.hasNext();     // 다음 페이지 존재 여부만 (true/false)
  }

  @Test
  void 댓글_추가(){
    Post post = postRepository.findById(1l).get();
    post.addComment(new Comment("첫 댓글"));
    post.addComment(new Comment("둘째 댓글"));
    postRepository.save(post);
    postRepository.delete(post);
  }
}
