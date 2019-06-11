package com.example.demo.bo;

public abstract class CheckFeasibility {

    public CheckFeasibility(){}
    public abstract String CheckAccountBalance(String phoneNumber, String totalAmount, String transactionId);
}
