package com.example.springcoditstudy.d1027.service;

import com.example.springcoditstudy.d1027.domain.*;
import com.example.springcoditstudy.d1027.domain.Payment1027.PayMethod;
import com.example.springcoditstudy.d1027.exception.BookNotFoundException;
import com.example.springcoditstudy.d1027.exception.MemberNotFoundException;
import com.example.springcoditstudy.d1027.exception.OrderNotFoundException;
import com.example.springcoditstudy.d1027.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.example.springcoditstudy.d1027.dto.Order1027LineRequest;
import com.example.springcoditstudy.d1027.dto.Order1027Response;

@Service
@RequiredArgsConstructor
public class Order1027Service {

    private final Member1027Repository memberRepository;
    private final Book1027Repository bookRepository;
    private final Order1027Repository orderRepository;
    private final Payment1027Repository paymentRepository;

    @Transactional
    public Long order(Long memberId, List<Order1027LineRequest> lines, PayMethod method) {

        Member1027 member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Order1027 order = Order1027.create(member);

        for (Order1027LineRequest line : lines) {
            Book1027 book = bookRepository.findById(line.getBookId())
                    .orElseThrow(() -> new BookNotFoundException(line.getBookId()));

            book.removeStock(line.getQuantity());          // 변경 감지 → UPDATE
            order.addOrderItem(new OrderItem1027(book, line.getQuantity()));
        }

        orderRepository.save(order);                    // cascade 로 OrderItem 까지

        paymentRepository.save(
                new Payment1027(order, order.getTotalPrice(), method));

        return order.getId();
    }

    // 엔티티가 아니라 DTO 로 바꿔서 반환한다. DTO 변환은 이 트랜잭션 안에서 끝낸다.
    @Transactional(readOnly = true)
    public Order1027Response getOrder(Long orderId) {
        Order1027 order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return Order1027Response.from(order);
    }

    @Transactional(readOnly = true)
    public List<Order1027Response> getOrders() {
        return orderRepository.findAll().stream()
                .map(Order1027Response::from)
                .toList();
    }

    @Transactional
    public void cancel(Long orderId) {
        Order1027 order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.cancel();        // save() 를 부르지 않는다
    }
}
