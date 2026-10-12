package com.example.springcoditstudy.d1001.repository;

import com.example.springcoditstudy.d1001.entity.Doctor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {

  // sql이 모두 JpaRepository에 담겨있다.
  // JpaRepository<Doctor,Long>를 통해서 Doctor테이블에 요청하는 모든 sql을 대신 만들어줌
  // 이제부터는 영속성 컨텍스트를 직접 다루지 않고 JpaRepository를 통해서 데이터를 관리하고 수정, 삭제한다.

  // save
  // findById
  // findAll
  // count
  // delete

  // 대신 구현체를 만들어준다.
  Optional<Doctor> findByName(String string);

  Optional<Doctor> findByNameAndLicenseNo(String name,String lino);

  List<Doctor> findByDepartment_Name(String string);

  // 페이지로 보여주기
  Page<Doctor> findByDepartment_Name(String string, Pageable pageable);

  // Slice<Doctor> findByDepartment_Name(String string, Pageable pageable);

  List<Doctor> findByDepartment_Id(Long departmentId);

  List<Doctor> findByDepartmentName(String name);

  boolean existsByLicenseNo(String no);
}
