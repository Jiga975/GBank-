package com.example.gbank.service;

import com.example.gbank.dto.BankResponse;
import com.example.gbank.dto.UserRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
}
