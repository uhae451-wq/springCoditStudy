package com.example.springcoditstudy.d0909.exception;

import com.example.springcoditstudy.d0909.attachment.ImageUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BController {

    @GetMapping("/b/error")
    public String test(){
        throw new ImageUploadException("error","error");
    }

    @GetMapping("/b/error2")
    public String test2(){
        throw new Exceptions("B페이지 예외 발생");
    }

    @GetMapping("/a/error3")
    public String error(){
        throw new IllegalArgumentException("에외발생");
    }

    @ExceptionHandler(Exceptions.class)
    public String Exception(Exceptions e,Model model){
        model.addAttribute("message",e.getMessage());
        return "errorPage";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),HttpStatus.NO_CONTENT);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
