package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDAO {

    private String PhoneNumber;
    private String AccessToken;
    private String State;
    private String Expires;
    private String CustomerId;



    public CustomerDAO() {
    }

    public CustomerDAO(CustomerDAO cust) {
        this.PhoneNumber = cust.getPhoneNumber();
        this.AccessToken = cust.getAccessToken();
        this.State = cust.getState();
        this.Expires= cust.getExpires();
        this.CustomerId= cust.getCustomerId();
    }

    public CustomerDAO(String phoneNumber, String accessToken, String state, String expires, String customerId) {
        PhoneNumber = phoneNumber;
        AccessToken = accessToken;
        State = state;
        Expires= expires;
        CustomerId= customerId;
    }


    public void retrieveData(String phoneNumber){
        try {

            Connection conn = DatabaseController.getConnection();
            PreparedStatement getstmt = conn.prepareStatement("SELECT * FROM Customer WHERE PhoneNumber LIKE ?");
            getstmt.setString(1, "%" + phoneNumber + "%");
            ResultSet rs = getstmt.executeQuery();
            // iterate through the java resultset
            while (rs.next()) {
                PhoneNumber = rs.getString(1);
                AccessToken = rs.getString(2);
                State = rs.getString(3);
                Expires=rs.getString(4);
                CustomerId=rs.getString(5);
            }
        }catch (Exception e){

            e.printStackTrace();
        }
    }

    public void insertData(String phoneNumber, String accessToken, String state, String expires, String customerId){
        try{
            Connection conn = DatabaseController.getConnection();
            PreparedStatement setstmt = conn.prepareStatement(
                    "INSERT INTO Customer (PhoneNumber, AccessToken, State, Expires, CustomerId) VALUES (?, ?,?,?,?);");
            setstmt.setString(1, phoneNumber);
            setstmt.setString(2, accessToken);
            setstmt.setString(3, state);
            setstmt.setString(4, expires);
            setstmt.setString(5, customerId);
            setstmt.execute();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateData(String phoneNumber, String accessToken, String state, String expires, String customerId){
        try{
            Connection conn = DatabaseController.getConnection();
            PreparedStatement setstmt = conn.prepareStatement(
                    "UPDATE Customer SET AccessToken = ?, State = ?,Expires=?, CustomerId=? WHERE PhoneNumber LIKE ?;");
            setstmt.setString(1, accessToken);
            setstmt.setString(2, state);
            setstmt.setString(3, expires);
            setstmt.setString(4, customerId);
            setstmt.setString(5, "%"+phoneNumber+"%");
            setstmt.execute();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //We need to call retrieve function from getCustomerId method

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getExpires() {
        return Expires;
    }

    public void setExpires(String expires) {
        Expires = expires;
    }
}
