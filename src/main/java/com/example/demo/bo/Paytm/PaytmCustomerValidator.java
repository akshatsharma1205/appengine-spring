package com.example.demo.bo.Paytm;

import com.example.demo.bo.CustomerValidator;
import com.example.demo.bo.AllocateTransactionId;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.TransactionsDAO;

public class PaytmCustomerValidator extends CustomerValidator {


    public  PaytmCustomerValidator(){

    }

    @Override
    public Boolean Validate(String phoneNumber, String totalAmount){
        CustomerDAO customer= new CustomerDAO();
        customer.retrieveData(phoneNumber);
        if(customer.getAccessToken()==null){
            return false;
        }
        else {
            ValidateToken token=new ValidateToken(phoneNumber,new CustomerDAO());
            token.validate_token();
            if (token.getResponseData().equalsIgnoreCase("true")){
                return true;
            }else{
                return false;
            }

        }

    }
}
