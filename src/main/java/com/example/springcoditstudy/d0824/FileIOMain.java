package com.example.springcoditstudy.d0824;

import java.util.Optional;

public class FileIOMain {
    public static void main(String[] args) {
        // 생성할 디렉토리, src와 같은 레벨
        String root = "fileio-practice";
        FileMemberRepository repository = new FileMemberRepository(root);

        // 1. Practice처럼 데이터를 준비하되, 각 회원을 별도 파일로 저장합니다.
        Member alice = new Member("Alice", "alice@example.com");
        Member bob = new Member("Bob", "bob@example.com");
        repository.save(alice);
        repository.save(bob);
        System.out.println("1. 저장 후 전체 조회: " + repository.findAll());

        // 2. 저장소 인스턴스를 새로 만들어도 파일에서 다시 읽습니다.
        FileMemberRepository reopened = new FileMemberRepository(root);
        Optional<Member> found = reopened.findById(alice.getId());
        if (found.isPresent()) {
            Member loaded = found.get();
            System.out.println("2. 파일에서 복원: " + loaded);
            // 3. 메모리의 객체만 바꾸면 파일은 바뀌지 않습니다.
            loaded.changeName("Alice 수정");
            System.out.println("3. save 전 파일 내용: " + reopened.findById(alice.getId()));
            reopened.save(loaded); // 같은 ID 파일을 덮어써서 수정 내용을 반영합니다.
            System.out.println("4. save 후 파일 내용: " + reopened.findById(alice.getId()));
        }
        reopened.deleteById(bob.getId());
        reopened.deleteById(alice.getId());
    }
}
