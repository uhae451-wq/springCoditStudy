package com.example.springcoditstudy.d1002.repository;

import com.example.springcoditstudy.d1002.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

}
