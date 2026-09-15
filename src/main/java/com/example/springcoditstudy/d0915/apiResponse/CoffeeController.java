package com.example.springcoditstudy.d0915.apiResponse;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoffeeController {

    @PostMapping("/coffee")
    public CoffeeResponse create(@RequestBody CreateCoffeeRequest request){
        Coffee coffee = new Coffee(1L,request.getName(),request.getPrice(),true);
        return CoffeeResponse.from(coffee);
    }

    @PostMapping("/coffee1")
    public CoffeeResponse create1(@Valid @RequestBody CreateCoffeeRequest request){
        Coffee coffee = new Coffee(1L,request.getName(),request.getPrice(),true);
        return CoffeeResponse.from(coffee);
    }

    @PostMapping("/coffee2")
    public ResponseEntity<ApiResponse<CoffeeResponse>> create2(@Valid @RequestBody CreateCoffeeRequest request){
        Coffee coffee = new Coffee(1L,request.getName(),request.getPrice(),true);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(CoffeeResponse.from(coffee)));
    }
}
