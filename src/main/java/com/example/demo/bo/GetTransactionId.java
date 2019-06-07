package com.example.demo.bo;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.TransactionsDAO;
import com.example.demo.dao.DatabaseController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class GetTransactionId {

    String PhoneNumber;
    String TransactionId;
    String TotalAmount;
    private static final Logger LOGGER = Logger.getLogger(DatabaseController.class.getName());


    public GetTransactionId() {
    }

    public GetTransactionId(String phoneNumber, String totalAmount) {
        PhoneNumber = phoneNumber;
        TotalAmount = totalAmount;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void Get_transactionID(){
        try {

            //Auto-Increment TransactionId and insert into table
            TransactionsDAO transaction =  new TransactionsDAO();
            TransactionId=transaction.getlastID()+1;
            transaction.insertData(TransactionId,PhoneNumber,TotalAmount,"Checking_Balance");


        }catch (Exception e){
            LOGGER.info("failed");
            e.printStackTrace();
        }
    }
}
