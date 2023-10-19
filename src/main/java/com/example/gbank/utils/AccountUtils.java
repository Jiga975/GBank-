package com.example.gbank.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE="001";
    public static final String ACCOUNT_EXISTS_MESSAGE="User already exists";
    public static final String ACCOUNT_CREATION_SUCCESS="002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE="Account has been created successfully";


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
