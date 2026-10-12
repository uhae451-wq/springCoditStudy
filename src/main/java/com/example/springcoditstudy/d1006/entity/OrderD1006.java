package com.example.springcoditstudy.d1006.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders_d1006")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderD1006 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime orderedAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private OrderStatus status = OrderStatus.ORDERED;

  @Column(nullable = false,updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Transient
  private int totalPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberD1006 member;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItemD1006> orderItems = new ArrayList<>();

  public static OrderD1006 create(MemberD1006 member) {
    OrderD1006 order = new OrderD1006();
    order.member = member;
    order.orderedAt = LocalDateTime.now();
    order.status = OrderStatus.ORDERED;
    return order;
  }

  public void addOrderItem(OrderItemD1006 item) {
    this.orderItems.add(item);
    item.assignOrder(this);
  }

  public int getTotalPrice(){
    return orderItems.stream().mapToInt(i -> i.getOrderPrice() * i.getQuantity()).sum();
  }

  public void cancel(){
    if(this.status == OrderStatus.CANCELED){
      throw new IllegalStateException("이미 취소된 주문입니다.");
    }
    this.status = OrderStatus.CANCELED;
    for(OrderItemD1006 i : orderItems){
      i.getBook().addStock(i.getQuantity());
    }
  }

  public enum OrderStatus{
    ORDERED,CANCELED
  }

}
