package com.example.demo.bo.Paytm;

import com.example.demo.bo.DebitMoney;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.TransactionsDAO;

public class PaytmDebitMoney extends DebitMoney {

    public PaytmDebitMoney(){}

    @Override
    public String Debit(String phoneNumber, String totalAmount, String transactionId){
        AutoDebit deb =new AutoDebit(phoneNumber,totalAmount,transactionId,new CustomerDAO(), new TransactionsDAO());
        deb.auto_debit();
        return deb.getResponseData();
    }
}
