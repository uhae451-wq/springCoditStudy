package com.example.springcoditstudy.d1001.repository;

import com.example.springcoditstudy.d1001.entity.Patient;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {

  // 해당 폰번호의 유무 확인
  boolean existsByPhone(String phone);

  // 키워드가 포함된 이름 검색
  List<Patient> findByNameContaining(String keyword);

  // 폰번호 찾기
  Optional<Patient> findByPhone(String phone);

  // 특정 날짜 이후에 태어난 환자를 이름으로 오름차순한 리스트
  List<Patient> findByBirthDateAfterOrderByNameAsc(LocalDate date);

}
