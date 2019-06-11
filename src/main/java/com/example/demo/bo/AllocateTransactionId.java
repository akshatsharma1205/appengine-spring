package com.example.demo.bo;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.TransactionsDAO;
import com.example.demo.dao.DatabaseController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public String getTransactionId() {
        return TransactionId;
    }

    public String Get_transactionID(){
        try {

            //Auto-Increment TransactionId and insert into table
            TransactionId=Integer.toString(Integer.parseInt(transaction.getlastID())+1);
            transaction.insertData(TransactionId,PhoneNumber,TotalAmount,"INIT_TXN");
            return TransactionId;


        }catch (Exception e){
            LOGGER.info("failed");
            return null;
        }
    }
}
