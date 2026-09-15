package com.example.springcoditstudy.d0915.responseTemplate;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {


    @PostMapping("/test1")
    public void test(@Valid @RequestBody RequestDTO testRequestDTO){
        System.out.println(testRequestDTO);
    }

    @PostMapping("/test2")
    public List<ResponseDTO> test2(@Valid @RequestBody RequestDTO testRequestDTO){
        // TestRequestDTO -> Test -> TestResponseDTO
        Test test = new Test(testRequestDTO.getName(),testRequestDTO.getEmail(),testRequestDTO.getAge());
        ResponseDTO testResponseDTO = new ResponseDTO(test.getName(), test.getEmail(), test.getAge());
        return List.of(testResponseDTO,testResponseDTO);
    }

    @PostMapping("/test3")
    public ResponseEntity<ApiResponse<ResponseDTO>> test3(@Valid @RequestBody RequestDTO testRequestDTO){
        // TestRequestDTO -> Test -> TestResponseDTO
        Test test = new Test(testRequestDTO.getName(),testRequestDTO.getEmail(),testRequestDTO.getAge());
        ResponseDTO testResponseDTO = new ResponseDTO(test.getName(), test.getEmail(), test.getAge());
        return ResponseEntity.ok(ApiResponse.success(testResponseDTO));
    }

    @PostMapping("/test4")
    public ResponseEntity<ApiResponse<ResponseDTO>> test4(@Valid @RequestBody RequestDTO testRequestDTO){
        throw new CustomException("customError");
    }


}
