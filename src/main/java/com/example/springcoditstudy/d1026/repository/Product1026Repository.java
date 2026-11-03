package com.example.springcoditstudy.d1026.repository;

import com.example.springcoditstudy.d1026.domain.Product1026;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Product1026Repository extends JpaRepository<Product1026,Long> {

  default Product1026 save(String name, int price, int stock){
    return save(new Product1026(name,price,stock));
  }

  // 재고가 몇개 이하인 제품 가져오기
  List<Product1026> findByStockLessThanEqual(int quantity);

  // 특정 단어가 들어간 제품 가져오기
  List<Product1026> findByNameContaining(String name);

  // 직접 작성한 쿼리 ( 가격범위 , 싼 순서대로 )
  @Query("select p from Product1026 p where p.price between :min and :max order by p.price")
  List<Product1026> findInPriceRange(@Param("min") int min, @Param("max") int max);

}
