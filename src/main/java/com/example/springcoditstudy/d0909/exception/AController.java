package com.example.springcoditstudy.d0909.exception;

import com.example.springcoditstudy.d0909.attachment.ImageUploadException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AController {

    @GetMapping("/a/error")
    public String test(){
        throw new ImageUploadException("A-errorMessage","A-errorCode");
    }

    @ExceptionHandler(ImageUploadException.class)
    public String handleException(ImageUploadException e, Model model){
        model.addAttribute("message","A에러페이지");
        return "errorPage";
    }

    @GetMapping("/a/error2")
    public String test2(){
        throw new Exceptions("A 페이지 예외 발생");
    }

    @ExceptionHandler(Exceptions.class)
    public String Exception(Exceptions e, Model model){
        model.addAttribute("message",e.getMessage());
        return "errorPage";
    }
}
