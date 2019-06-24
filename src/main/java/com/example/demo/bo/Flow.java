package com.example.demo.bo;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.TransactionsDAO;

public abstract class Flow {


    private String CheckRegisteredResponse=null;
    private Boolean InitiateBool=false;
    private String DebitResponse=null;

    public CustomerValidator customerValidator;
    public DebitMoney debitMoney;
    public CheckFeasibility checkFeasibility;

    public Flow(){}

    //GENERAL APIs FLOW SPLITTER

    public String CheckRegistered (String phoneNumber, String totalAmount,String mode){

        if (customerValidator.Validate(phoneNumber, totalAmount)){
            AllocateTransactionId allocate=new AllocateTransactionId(phoneNumber,totalAmount,new TransactionsDAO());
            return allocate.Get_transactionID(mode);
        }else{
            return "false";
        }

    }

    public String Inititate( String phoneNumber, String totalAmount, String transactionId ){

        return checkFeasibility.CheckAccountBalance(phoneNumber, totalAmount, transactionId);

    }

    public String Debit(String phoneNumber, String totalAmount, String transactionId){

        return debitMoney.Debit(phoneNumber, totalAmount, transactionId);

    }

}

