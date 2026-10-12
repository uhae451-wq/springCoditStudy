package com.example.springcoditstudy.d1006.repository;

import com.example.springcoditstudy.d1006.entity.BookD1006;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookD1006,Long> {

  Page<BookD1006> findByCategoryId(Long categoryId, Pageable pageable);

  List<BookD1006> findByTitleContaining(String keyword);

  List<BookD1006> findByStockLessThan(int stork);

  // 이름으로 쓰면 읽기 힘든 조건은 JPQL 로
  @Query("select b from BookD1006 b " + "where b.stock <= :stock and b.category.id = :categoryId " + "order by b.price desc")
  List<BookD1006> findLowStockInCategory(@Param("stock") int stock,@Param("categoryId") Long categoryId);
}
