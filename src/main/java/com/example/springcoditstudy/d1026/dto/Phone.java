package com.example.springcoditstudy.d1026.dto;

import com.example.springcoditstudy.d1026.domain.BannedWordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 필드에만 붙일수 있게한다.
@Retention(RetentionPolicy.RUNTIME) // 서버가 실행되는 동안 정보가 남게 하겠다.
@Constraint(validatedBy = BannedWordValidator.class)
public @interface Phone {

  String message() default "전화번호 형식이 아닙니다 (예: 010-1234-5678)";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
