package com.example.springcoditstudy.d1027.domain;

import com.example.springcoditstudy.d1027.exception.InvalidQuantityException;
import com.example.springcoditstudy.d1027.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books_d1027")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book1027 {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category1027 category;

    public Book1027(String title, String author, int price, int stock) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
    }

    public void assignCategory(Category1027 category) { this.category = category; }

    // 재고 차감 — 변경 감지로 UPDATE가 나간다. save() 를 부르지 않는다
    public void removeStock(int quantity) {
        // 음수를 그냥 두면 재고가 오히려 늘어납니다. 입구의 @Min(1) 과 별개로 엔티티도 스스로 막습니다.
        if (quantity < 1) {
            throw new InvalidQuantityException(quantity);
        }
        int rest = this.stock - quantity;
        if (rest < 0) {
            throw new OutOfStockException(this.title, this.stock, quantity);
        }
        this.stock = rest;
    }

    public void addStock(int quantity) {
        if (quantity < 1) {
            throw new InvalidQuantityException(quantity);
        }
        this.stock += quantity;
    }
}
