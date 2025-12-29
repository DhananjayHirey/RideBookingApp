package com.rideApp.BFF.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController{

    @GetMapping("/auth/register")
    public void register(@RequestBody String id){
        System.out.println(id);
    }

}
