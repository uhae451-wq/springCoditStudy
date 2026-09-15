package com.example.springcoditstudy.d0915.responseTemplate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @Email(message = "메세지 형식이 아닙니다.")
    private String email;

    @Min(value = 1, message = "1이상의 양수를 입력해주세요.")
    @Max(value = 100, message = "100이하의 수를 입력해줏에요.")
    private int age;
}
