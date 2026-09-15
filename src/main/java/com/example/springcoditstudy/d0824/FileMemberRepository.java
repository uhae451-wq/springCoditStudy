package com.example.springcoditstudy.d0824;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileMemberRepository {

    private final Path directory;

    // 1. 저장 폴더 준비: root/member
    public FileMemberRepository(String root) {
        directory = Path.of(root).resolve("member");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new UncheckedIOException("저장 폴더 생성 실패: " + directory, e);
        }
    }

    // Path는 경로를 표현합니다. resolve 자체가 파일을 만들지는 않습니다.
    private Path path(UUID id) {
        return directory.resolve(id + ".ser");
    }

    // 2. 저장: Member -> 바이트 -> UUID.ser
    // 새 ID면 생성, 같은 ID면 덮어쓰기이므로 수정에도 같은 메서드를 씁니다.
    public Member save(Member member) {
        Path file = path(member.getId());
        // 바깥 ObjectOutputStream을 닫으면 내부 파일 스트림도 닫힙니다.
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(member);
        } catch (IOException e) {
            // 호출자에게 실패를 알리고, 원인 e를 보존합니다.
            throw new UncheckedIOException("저장 실패: " + file, e);
        }
        return member;
    }

    // 3. 개별 조회: UUID -> 파일 경로 -> 바이트 -> Member
    public Optional<Member> findById(UUID id) {
        Path file = path(id);
        if (Files.notExists(file)) {
            // Optional.empty(): 정상적으로 조회했지만 해당 회원이 없음.
            return Optional.empty();
        }
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            // readObject의 반환 타입은 Object라서 Member로 형변환합니다.
            return Optional.of((Member) in.readObject());
        } catch (IOException e) {
            // 읽기 실패를 '회원 없음'으로 숨기지 않습니다.
            throw new UncheckedIOException("조회 실패: " + file, e);
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IllegalStateException("잘못된 저장 데이터: " + file, e);
        }
    }

    // 4. 전체 조회: 파일 목록 -> 각 파일의 ID -> findById -> 회원 목록
    public List<Member> findAll() {
        List<Member> result = new ArrayList<>();
        // Files.list도 디렉터리 자원을 사용하므로 반드시 닫습니다.
        try (Stream<Path> files = Files.list(directory)) {
            for (Path file : files.toList()) {
                String name = file.getFileName().toString();
                if (name.endsWith(".ser")) {
                    // "UUID.ser"에서 끝의 네 글자(.ser)를 제거합니다.
                    UUID id = UUID.fromString(name.substring(0, name.length() - 4));
                    Optional<Member> member = findById(id);
                    if (member.isPresent()) {
                        result.add(member.get());
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("목록 조회 실패: " + directory, e);
        }
        return result;
    }

    // 5. 조건 조회: 파일 이름에 이메일이 없으므로 내용을 읽고 비교합니다.
    public Optional<Member> findByEmail(String email) {
        for (Member member : findAll()) {
            if (member.getEmail().equals(email)) {
                return Optional.of(member);
            }
        }
        return Optional.empty();
    }

    // 6. 존재 확인은 역직렬화 없이 경로만 확인합니다.
    public boolean existsById(UUID id) {
        return Files.exists(path(id));
    }

    // 7. 객체 하나가 파일 하나이므로 해당 파일만 삭제합니다.
    public void deleteById(UUID id) {
        try {
            // 파일이 이미 없어도 예외 없이 끝납니다.
            Files.deleteIfExists(path(id));
        } catch (IOException e) {
            throw new UncheckedIOException("삭제 실패: " + id, e);
        }
    }
}
