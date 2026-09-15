package com.example.springcoditstudy.d0909.practice;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PracticeController {

    private final TrainerService trainerService;

    @GetMapping("/v1/trainers/header")
    public String getUserAgent(@RequestHeader("User-Agent")String userAgent, Model model){
        System.out.println("User-Agent : "+userAgent);
        model.addAttribute("userAgent",userAgent);
        return "test";
    }

    @PostMapping(value="/v1/trainers/json")
    public String postTrainerJson(@RequestBody Trainer trainer){
        System.out.println(trainer);
        return "test";
    }

    @GetMapping("/v1/trainers/set-cookie")
    public String setCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("trainerToken","kim-hs");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60);
        response.addCookie(cookie);
        return "test";
    }

    @GetMapping("/v1/trainers/cookie")
    public String getCookieToken(@CookieValue("trainerToken")String token ){
        System.out.println(token);
        return "test";
    }
}
