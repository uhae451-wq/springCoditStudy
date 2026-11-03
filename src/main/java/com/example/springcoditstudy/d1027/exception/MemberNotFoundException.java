package com.example.springcoditstudy.d1027.exception;

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException(Long id) {
        super(ErrorCode.MEMBER_NOT_FOUND, "회원을 찾을 수 없습니다: " + id);
    }
}
