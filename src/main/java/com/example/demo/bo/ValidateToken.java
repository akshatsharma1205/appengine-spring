package com.example.demo.bo;


import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.DatabaseController;
import com.example.demo.model.ValidateTokenRequest;
import com.example.demo.model.ValidateTokenResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class  ValidateToken {

    private ValidateTokenRequest request;
    private String responseData;

    public ValidateToken(String phonenumber) {
        this.request=new ValidateTokenRequest(phonenumber);
    }




    public void validate_token() {
        try {

            CustomerDAO customer = new CustomerDAO();
            customer.retrieveData(request.getPhonenumber());
            request.setSessionToken(customer.getAccessToken());

            //HIT API
            accessURL();

            //Evaluate response:
            //TODO error handling for failure response
            Gson g=new Gson();
            ValidateTokenResponse validateTokenResponse= g.fromJson(responseData,ValidateTokenResponse.class);
            customer.updateData(request.getPhonenumber(),customer.getAccessToken(),null,validateTokenResponse.getExpires(),validateTokenResponse.getId());


            //Affirmative response to data
            responseData="exists";

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void accessURL(){
        try {
            // TODO production URL transactionURL = new URL("https://accounts.paytm.com/user/details");
            java.net.URL transactionURL = new java.net.URL("https://accounts-uat.Paytm.com/user/details");
            HttpURLConnection connection = (HttpURLConnection) transactionURL.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("session_token", request.getSessionToken());
            connection.setUseCaches(false);
            connection.setDoOutput(true);


            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response Json = " + responseData);
            }
            responseReader.close();
        }catch(Exception e){}
    }

    public String getResponseData() {
        return responseData;
    }

}
