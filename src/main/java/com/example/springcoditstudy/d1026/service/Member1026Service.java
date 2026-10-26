package com.example.springcoditstudy.d1026.service;

import com.example.springcoditstudy.d1026.domain.Member1026;
import com.example.springcoditstudy.d1026.exception.BaseException;
import com.example.springcoditstudy.d1026.exception.DuplicatEmailException;
import com.example.springcoditstudy.d1026.exception.ErrorCode;
import com.example.springcoditstudy.d1026.repository.Member1026Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Member1026Service {
  private final Member1026Repository memberRepository;

  public Member1026 join(String name, String email, int age) {
    if(memberRepository.existsByEmail(email)){
      throw new DuplicatEmailException(ErrorCode.DUPLICATE_EMAIL);
    }
    return memberRepository.save(name, email, age);
  }

  public Member1026 getMember(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND) {
        });
  }


}
