package com.example.demo.bo.Paytm;

import com.example.demo.bo.Flow;

public class PaytmFlow extends Flow {

    public PaytmFlow()
    {
        super.customerValidator = new PaytmCustomerValidator();
        super.debitMoney= new PaytmDebitMoney();
        super.checkFeasibility= new PaytmCheckFeasibility();
    }

}
