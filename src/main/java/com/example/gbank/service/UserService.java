package com.example.gbank.service;

import com.example.gbank.dto.BankResponse;
import com.example.gbank.dto.CreditDebitRequest;
import com.example.gbank.dto.EnquiryRequest;
import com.example.gbank.dto.UserRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
}
