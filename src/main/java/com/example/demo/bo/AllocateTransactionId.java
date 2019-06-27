package com.example.demo.bo;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.TransactionsDAO;
import com.example.demo.dao.DatabaseController;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
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

            //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            Date date = new Date();
            sd.setTimeZone(TimeZone.getTimeZone("IST"));
            TransactionId= UUID.randomUUID().toString();
            transaction.insertData(sd.format(date),TransactionId,PhoneNumber,TotalAmount,"INIT_TXN",paymentMode);
            return TransactionId;


        }catch (Exception e){
            LOGGER.info("failed");
            return null;
        }
    }
}
