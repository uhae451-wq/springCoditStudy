package com.example.springcoditstudy.d1027;

import com.example.springcoditstudy.d1026.repository.Product1026Repository;
import com.example.springcoditstudy.d1026.service.Product1026Service;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class Product1026ServiceTest {

  @Mock
  private Product1026Repository product1026Repository;

  @InjectMocks
  private Product1026Service product1026Service;
}
