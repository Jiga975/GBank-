package com.example.gbank.service.serviceImp;

import com.example.gbank.dto.*;
import com.example.gbank.model.User;
import com.example.gbank.repository.UserRepository;
import com.example.gbank.service.EmailService;
import com.example.gbank.service.UserService;
import com.example.gbank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

//++++++++++++++++++++++++++++++ CUSTOMER ACCOUNT CREATION SERVICE +++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        //instantiate the user class to create a new user
        if (userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);

// +++++++++++++++++++++++++++++++++ EMAIL SERVICE IMPLEMENTATION ++++++++++++++++++++++++++++++++++++++++++++
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Notice of New Account Creation")
                .messageBody("Dear "+savedUser.getFirstName()+"\n" +" Welcome to GBank , No Fear na Onyeka dey test his bank app lol\n Your Account has been successfully created and "+
                        "Your new Account Number is "+savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName()+" "
                                +savedUser.getOtherName()+" "+savedUser.getLastName())
                        .build())
                .build();
    }
//+++++++++++++++++++++++++Account Enquiries Function+++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        //check if the provided account number exists in our db
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser= userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName()+" " +
                                " "+foundUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }

        User foundUser =userRepository.findByAccountNumber(request.getAccountNumber());

        return foundUser.getFirstName()+" "+foundUser.getOtherName()+" " + " "+foundUser.getOtherName();
    }
//+++++++++++++++++++++++++++++Account Credit functionality ++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        //1. check if account exist
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName())
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }
//+++++++++++++++++++++++++++++++ Account Debit Functionality +++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        //1. check if account exist
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //2. check if the amount in the account is enough to be debited from
        User userToDebit =userRepository.findByAccountNumber(request.getAccountNumber());
        //convert bigDecimal to integer so that you can be able to run  the comparison
        int availableBalance = Integer.parseInt(userToDebit.getAccountNumber());
        int debitAmount = Integer.parseInt(request.getAccountNumber().toString());
        if (availableBalance<debitAmount){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(request.getAccountNumber())
                            .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }



    }

}
