package com.example.gbank.controller;

import com.example.gbank.dto.BankResponse;
import com.example.gbank.dto.UserRequest;
import com.example.gbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping
    public BankResponse creatAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }
}
