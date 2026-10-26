package com.example.springcoditstudy.d1026.controller;

import com.example.springcoditstudy.d1026.domain.Member1026;
import com.example.springcoditstudy.d1026.dto.MemberRequest;
import com.example.springcoditstudy.d1026.service.Member1026Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class Member1026Controller {

  private final Member1026Service memberService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Member1026 join(@RequestBody MemberRequest request) {
    return memberService.join(request.getName(), request.getEmail(), request.getAge());
  }

  @GetMapping("/{id}")
  public Member1026 get(@PathVariable Long id) {
    return memberService.getMember(id);
  }
}
