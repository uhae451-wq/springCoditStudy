package com.example.springcoditstudy.d1001;

import com.example.springcoditstudy.d1001.service.ReservationConfirmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController1002 {

  private final ReservationConfirmService reservationService;

  @GetMapping("/test/1")
  public void test() throws PaymentFiledException {
    reservationService.conform(4l,100  ,false);
  }

}
