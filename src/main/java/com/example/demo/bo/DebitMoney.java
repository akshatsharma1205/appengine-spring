package com.example.demo.bo;

public abstract class DebitMoney {

    public DebitMoney(){}
    public abstract String Debit(String phoneNumber, String totalAmount, String transactionId);

}
