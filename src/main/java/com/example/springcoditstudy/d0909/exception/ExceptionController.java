package com.example.springcoditstudy.d0909.exception;

import com.example.springcoditstudy.d0909.attachment.ImageUploadException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ImageUploadException.class)
    public String handleException(ImageUploadException e, Model model){
        model.addAttribute("message","C에러페이지");
        return "errorPage";
    }

    @ExceptionHandler(Exceptions.class)
    public String Exception(Exceptions e,Model model){
        model.addAttribute("message","C페이지 에러 페이지");
        return "errorPage";
    }
}