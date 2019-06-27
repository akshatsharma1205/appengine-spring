package com.example.demo.bo;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.TransactionsDAO;
import com.example.demo.dao.DatabaseController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.logging.Logger;

public class AllocateTransactionId {

    String PhoneNumber;
    String TransactionId;
    String TotalAmount;
    private TransactionsDAO transaction;
    private static final Logger LOGGER = Logger.getLogger(DatabaseController.class.getName());


    public AllocateTransactionId() {
    }

    public AllocateTransactionId(String phoneNumber, String totalAmount, TransactionsDAO trans) {
        this.PhoneNumber = phoneNumber;
        this.TotalAmount = totalAmount;
        this.transaction=new TransactionsDAO(trans);
    }


    public String Get_transactionID(String paymentMode){
        try {

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            TransactionId= UUID.randomUUID().toString();
            transaction.insertData(timestamp.toString(),TransactionId,PhoneNumber,TotalAmount,"INIT_TXN",paymentMode);
            return TransactionId;


        }catch (Exception e){
            LOGGER.info("failed");
            return null;
        }
    }
}
