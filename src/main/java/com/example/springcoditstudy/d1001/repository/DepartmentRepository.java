package com.example.springcoditstudy.d1001.repository;

import com.example.springcoditstudy.d1001.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {

}
