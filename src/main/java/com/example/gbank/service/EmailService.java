package com.example.gbank.service;

import com.example.gbank.dto.EmailDetails;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
