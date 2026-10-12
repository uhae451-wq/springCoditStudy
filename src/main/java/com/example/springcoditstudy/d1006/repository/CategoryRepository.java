package com.example.springcoditstudy.d1006.repository;

import com.example.springcoditstudy.d1006.entity.CategoryD1006;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryD1006,Long> {

}
