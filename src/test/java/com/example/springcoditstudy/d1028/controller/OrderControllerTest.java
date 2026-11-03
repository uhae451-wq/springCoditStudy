package com.example.springcoditstudy.d1028.controller;

import com.example.springcoditstudy.d1027.controller.*;
import com.example.springcoditstudy.d1027.domain.*;
import com.example.springcoditstudy.d1027.dto.*;
import com.example.springcoditstudy.d1027.exception.*;
import com.example.springcoditstudy.d1027.repository.*;
import com.example.springcoditstudy.d1027.service.*;

import com.example.springcoditstudy.d1027.exception.OrderNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;   // ← Boot 4 에서 패키지가 바뀌었습니다
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;            // when, verify, never
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;   // post, get, delete
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;     // status, jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // andDo(print())


@WebMvcTest(Order1027Controller.class)
class OrderControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  Order1027Service orderService;

  @Test
  void 없는_주문을_조회하면_404가_나간다() throws Exception {
    when(orderService.getOrder(99999L))
        .thenThrow(new OrderNotFoundException(99999L));

    mockMvc.perform(get("/d1027/v1/orders/99999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("ORDER_NOT_FOUND"));
  }

  @Test
  @DisplayName("")
  void order_path_valiable_case_01() throws Exception {
    // given

    // when then
    mockMvc.perform(get("/d1027/v1/orders/{orderId}",1L))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("")
  void book_delete_case_01() throws Exception {
    // given
    // when then
    mockMvc.perform(delete("/d1027/v1/orders/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void test_01() throws Exception {
    mockMvc.perform(get("/d1027/v1/orders/1"))
        .andDo(print())                    // 요청·응답 전체를 콘솔에
        .andExpect(status().isOk());
  }

}