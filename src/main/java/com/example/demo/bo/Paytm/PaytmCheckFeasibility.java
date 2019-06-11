package com.example.demo.bo.Paytm;

import com.example.demo.bo.CheckFeasibility;
import com.example.demo.dao.CustomerDAO;

public class PaytmCheckFeasibility extends CheckFeasibility {
    public PaytmCheckFeasibility(){}

    @Override
    public String CheckAccountBalance(String phoneNumber, String totalAmount, String transactionId){
        CheckBalance check=new CheckBalance(phoneNumber,totalAmount,transactionId,new CustomerDAO());
        check.check_balance();
        if (!check.getResponse()){
            return "false";
        }
        else{
            return "true";
        }
    }
}
