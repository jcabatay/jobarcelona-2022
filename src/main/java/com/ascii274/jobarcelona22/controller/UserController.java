package com.ascii274.jobarcelona22.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/jobarcelona")
public class UserController {

    @GetMapping (value="/test")
    public String hello(){
        return "Hello testing jobarcelona 2022";
    }

    @GetMapping (value = "/login")
    public String login(){
        return "You are in login";
    }

    @PutMapping (value = "/signup")
    public String signup(){
        return "Sign out";
    }


}
