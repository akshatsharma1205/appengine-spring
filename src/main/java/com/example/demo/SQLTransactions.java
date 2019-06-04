package com.example.demo;

import java.sql.ResultSet;

public class SQLTransactions {

    String TransactionId;
    String PhoneNumber;
    String Amount;
    String Status;
    public SQLTransactions() {
    }

    public SQLTransactions(String transactionId, String phoneNumber, String amount, String status) {
        TransactionId = transactionId;
        PhoneNumber = phoneNumber;
        Amount = amount;
        Status = status;
    }

    public void SQLRetrieve(ResultSet rs){

        try {
            // iterate through the java resultset
            while (rs.next()) {
                TransactionId = rs.getString(1);
                PhoneNumber = rs.getString(2);
                Amount = rs.getString(3);
                Status=rs.getString(4);
            }
        }catch (Exception e){

            e.printStackTrace();
        }
    }



    public String getTransactionId() {
        return TransactionId;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getAmount() {
        return Amount;
    }

    public String getStatus() {
        return Status;
    }
}
