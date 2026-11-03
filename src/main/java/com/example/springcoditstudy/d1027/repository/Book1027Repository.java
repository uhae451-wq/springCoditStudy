package com.example.springcoditstudy.d1027.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import com.example.springcoditstudy.d1027.domain.Book1027;

public interface Book1027Repository extends JpaRepository<Book1027, Long> {

    Page<Book1027> findByCategoryId(Long categoryId, Pageable pageable);

    List<Book1027> findByTitleContaining(String keyword);

    List<Book1027> findByStockLessThan(int stock);

    // 이름으로 쓰면 읽기 힘든 조건은 JPQL 로
    @Query("select b from Book1027 b " +
           "where b.stock <= :stock and b.category.id = :categoryId " +
           "order by b.price desc")
    List<Book1027> findLowStockInCategory(@Param("stock") int stock,
                                      @Param("categoryId") Long categoryId);
}
