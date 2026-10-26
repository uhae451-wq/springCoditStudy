package com.example.springcoditstudy.d1026.domain;

import com.example.springcoditstudy.d1026.dto.BannedWord;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class BannedWordValidator implements ConstraintValidator<BannedWord,String> {

  private static final List<String> BANNED = List.of("짝퉁","가짜","불법","마약");

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if(value == null){
      return true;
    }
    return BANNED.stream().noneMatch(value::contains);
  }
}
