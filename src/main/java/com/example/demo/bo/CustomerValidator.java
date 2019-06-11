package com.example.demo.bo;


public abstract class CustomerValidator {

    public CustomerValidator(){}
    public abstract Boolean Validate(String phoneNumber, String totalAmount);
}
