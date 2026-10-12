package com.example.springcoditstudy.d1002.repository;

import com.example.springcoditstudy.d1002.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

}
