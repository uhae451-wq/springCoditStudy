package com.example.springcoditstudy.d1027.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items_d1027")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem1027 {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order1027 order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book1027 book;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int orderPrice;   // 주문 시점의 가격을 박아둔다

    public OrderItem1027(Book1027 book, int quantity) {
        this.book = book;
        this.quantity = quantity;
        this.orderPrice = book.getPrice();   // ← 지금 가격을 복사
    }

    void assignOrder(Order1027 order) { this.order = order; }
}
