package com.example.springcoditstudy.d1027.domain;

import com.example.springcoditstudy.d1027.exception.AlreadyCanceledException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders_d1027")   // ← order 는 예약어라 반드시 필요하다
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order1027 {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member1027 member;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY,
               cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem1027> orderItems = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime orderedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.ORDERED;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public static Order1027 create(Member1027 member) {
        Order1027 order = new Order1027();
        order.member = member;
        order.orderedAt = LocalDateTime.now();
        order.status = OrderStatus.ORDERED;
        return order;
    }

    public void addOrderItem(OrderItem1027 item) {
        this.orderItems.add(item);
        item.assignOrder(this);
    }

    // 컬럼으로 만들지 않고, 주문항목에서 계산한다
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(i -> i.getOrderPrice() * i.getQuantity())
                .sum();
    }

    public void cancel() {
        if (this.status == OrderStatus.CANCELED) {
            throw new AlreadyCanceledException(this.id);
        }
        this.status = OrderStatus.CANCELED;
        for (OrderItem1027 item : orderItems) {
            item.getBook().addStock(item.getQuantity());   // 재고 복구
        }
    }

    public enum OrderStatus { ORDERED, CANCELED }
}
