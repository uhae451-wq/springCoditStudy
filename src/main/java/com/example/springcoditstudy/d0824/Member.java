package com.example.springcoditstudy.d0824;

import java.io.Serializable;
import java.util.UUID;

// Practice.User에서 확장: 파일 이름에 사용할 id를 추가합니다.
// 미션은 Common이 Serializable을 구현하고 User 등이 이를 상속합니다.
public class Member implements Serializable {
    // 저장 당시 클래스와 현재 클래스의 직렬화 버전을 비교할 때 사용합니다.
    // 이 값을 유지한다고 모든 필드 변경이 호환되는 것은 아닙니다.
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private String name;
    private final String email;

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public void changeName(String name) { this.name = name; }

    @Override
    public String toString() {
        return name + "(" + email + ", id=" + id + ")";
    }
}
