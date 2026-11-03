package com.example.springcoditstudy.d1027.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments_d1027")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment1027 {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order1027 order;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PayMethod method;

    @Column(nullable = false, updatable = false)
    private LocalDateTime paidAt = LocalDateTime.now();

    public Payment1027(Order1027 order, int amount, PayMethod method) {
        this.order = order;
        this.amount = amount;
        this.method = method;
    }

    public enum PayMethod { CARD, TRANSFER, POINT }
}
