package com.example.demo.bo.Paytm;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.DatabaseController;
import com.example.demo.model.Paytm.ValidateOtpRequest;
import com.example.demo.model.Paytm.ValidateOtpResponse;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.TreeMap;

public class ValidateOtp {


    
    private ValidateOtpRequest request;
    private CustomerDAO customer;
    String responseData;
    
    public String getResponseData() {
        return responseData;
    }
    
    public ValidateOtp(String number, String otp,CustomerDAO cust) {
        this.request=new ValidateOtpRequest(number,otp);
        this.customer=new CustomerDAO(cust);

    }

    
    public void validate_OTP() {
        try {

            //retrieve state for given phone number
            customer.retrieveData(request.getPhonenumber());
            request.setState(customer.getState());

            //Hit Paytm API
            accessURL();

            //writing accesstoken to Customer Table
            //TODO error handling for failure response
            Connection conn = DatabaseController.getConnection();
            Gson g=new Gson();
            customer.updateData(request.getPhonenumber(), g.fromJson(responseData,ValidateOtpResponse.class).getAccess_token(), null, null, null);


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void accessURL(){
        //TODO hard -coded auth with clientID
        String auth = "Basic " + "bWVyY2hhbnQtcGVycHVsZS1zdGc6amxnQkt5Z0NhZ3A2UEtFQ01FYkxCZkN2aUpvcmxlQWo=";

        TreeMap<String, String> paytmParams = new TreeMap<String, String>();
        paytmParams.put("otp", request.getOtp());
        paytmParams.put("state", request.getState());

        try{
            URL transactionURL = new URL("https://accounts-uat.paytm.com/signin/validate/otp");

            JSONObject obj = new JSONObject(paytmParams);
            String postData = obj.toString();
            HttpURLConnection connection = (HttpURLConnection) transactionURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Authorization", auth);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(postData);
            requestWriter.close();

            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response Json = " + responseData);
            }
            System.out.append("Requested Json = " + postData + " ");
            responseReader.close();

        }catch(Exception e){}
    }
    
}
