package com.example.gbank.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE="001";
    public static final String ACCOUNT_EXISTS_MESSAGE="User already exists";
    public static final String ACCOUNT_CREATION_SUCCESS="002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE="Account has been created successfully";
    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "Account doesn't Exist";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_SUCCESS = "Account found";
    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Account credited Successfully";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Account debited Successfully";


    //here I am generating I wish to generate random account numbers with the first 4 digits being the \
    //the years numbers and the last six a random number from 100000 to 999999
    public static String generateAccountNumber(){
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        //generating random number between max and min
        int randNumber = (int)Math.floor(Math.random()*(max-min+1)+min);

//convert current year and random number to string and concatenate them
        String year = String.valueOf(currentYear);
        String randomNumber=String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomNumber).toString();

    }
}
