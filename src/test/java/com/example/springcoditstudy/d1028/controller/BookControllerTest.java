package com.example.springcoditstudy.d1028.controller;

import com.example.springcoditstudy.d1027.controller.*;
import com.example.springcoditstudy.d1027.domain.*;
import com.example.springcoditstudy.d1027.dto.*;
import com.example.springcoditstudy.d1027.exception.*;
import com.example.springcoditstudy.d1027.repository.*;
import com.example.springcoditstudy.d1027.service.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;   // ← Boot 4 에서 패키지가 바뀌었습니다
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;            // when, verify, never
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;   // post, get, delete
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;     // status, jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // andDo(print())
import static org.hamcrest.Matchers.*;          // hasItem, notNullValue


@WebMvcTest(Book1027Controller.class)
class BookControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  Book1027Service bookService;

  @Test
  @DisplayName("")
  void success_book_create_case_01() throws Exception {
    // given
    String json = """
        {
          "title": "책제목",
          "author" : "김학수",
          "price" : 10000,
          "stock" : 20
        }
        """;
    when(bookService.create(any())).thenReturn(1l);
    // when then
    mockMvc.perform(post("/d1027/v1/books")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath(("$.id")).value(1)
        );
  }

  @Test
  @DisplayName("")
  void fail_book_create_case_01() throws Exception {
    // given
    String json = """
        {
          "title": "",
          "author" : "",
          "price" : 10000,
          "stock" : 20
        }
        """;
    // when then
    mockMvc.perform(post("/d1027/v1/books")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
        .andExpect(jsonPath("$.errors[*].field").value(hasItem("title")))
        .andExpect(jsonPath("$.errors[*].field").value(hasItem("author")));

  }

  @Test
  @DisplayName("")
  void book_query_param_case_01() throws Exception {
    // given
    // when then
    mockMvc.perform(get("/d1027/v1/books")
        .param("categoryId","1")
        .param("page","1")
        .param("size","1"))
        .andExpect(status().isOk());
  }

}