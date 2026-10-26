package com.example.springcoditstudy.d1026.domain;

import com.example.springcoditstudy.d1026.dto.BannedWord;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class PhoneValidator implements ConstraintValidator<BannedWord,String> {

  private static final String PHONE_REGEX = "^010-\\d{4}-\\d{4}$";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    return value.matches(PHONE_REGEX);
  }
}
