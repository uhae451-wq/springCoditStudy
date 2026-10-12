package com.example.springcoditstudy.d1006.service;

import com.example.springcoditstudy.d1006.dto.OrderLineRequest;
import com.example.springcoditstudy.d1006.dto.OrderResponse;
import com.example.springcoditstudy.d1006.entity.BookD1006;
import com.example.springcoditstudy.d1006.entity.MemberD1006;
import com.example.springcoditstudy.d1006.entity.OrderD1006;
import com.example.springcoditstudy.d1006.entity.OrderItemD1006;
import com.example.springcoditstudy.d1006.entity.PaymentD1006;
import com.example.springcoditstudy.d1006.entity.PaymentD1006.PayMethod;
import com.example.springcoditstudy.d1006.exception.BookNotFoundException;
import com.example.springcoditstudy.d1006.exception.MemberNotFoundException;
import com.example.springcoditstudy.d1006.exception.OrderNotFoundException;
import com.example.springcoditstudy.d1006.repository.BookRepository;
import com.example.springcoditstudy.d1006.repository.MemberRepositoryD1006;
import com.example.springcoditstudy.d1006.repository.OrderRepository;
import com.example.springcoditstudy.d1006.repository.PaymentRepositoryD1006;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final MemberRepositoryD1006 memberRepository;
  private final BookRepository bookRepository;
  private final PaymentRepositoryD1006 paymentRepository;

  @Transactional
  public Long order(Long memberId, List<OrderLineRequest> lines, PayMethod method){
    MemberD1006 member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
    OrderD1006 order = OrderD1006.create(member);

    for(OrderLineRequest i : lines){
      BookD1006 book = bookRepository.findById(i.getBookId()).orElseThrow(()-> new BookNotFoundException(i.getBookId()));
      book.removeStock(i.getQuantity());
      order.addOrderItem(new OrderItemD1006(book,i.getQuantity()));
    }
    orderRepository.save(order);

    // if (true) throw new RuntimeException("결제 직전 실패");

    paymentRepository.save(new PaymentD1006(order,order.getTotalPrice(),method));

    return order.getId();
  }

  @Transactional(readOnly = true)
  public OrderResponse getOrder(Long orderId){
    OrderD1006 order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException(orderId));
    return OrderResponse.from(order);
  }

  @Transactional(readOnly = true)
  public List<OrderResponse> getOrders(){
    List<OrderD1006> orders = orderRepository.findAll();
    return orders.stream().map(OrderResponse::from).toList();
  }

  @Transactional
  public void cancel(Long orderId){
    OrderD1006 order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException(orderId));
    order.cancel();
  }

}
