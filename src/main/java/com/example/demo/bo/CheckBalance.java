package com.example.demo.bo;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.CheckBalanceRequest;
import com.example.demo.model.CheckBalanceResponse;
import com.example.demo.dao.DatabaseController;
import com.google.gson.Gson;
import com.paytm.pg.merchant.CheckSumServiceHelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class CheckBalance {
    String responseData;

    CheckBalanceRequest request;
    HttpURLConnection connection = null;

    private static final Logger LOGGER = Logger.getLogger(DatabaseController.class.getName());

    public CheckBalance() {
    }

    public CheckBalance(String phonenumber,String bill, String orderId) {
        this.request=new CheckBalanceRequest(phonenumber,bill,orderId);
    }


    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }


    public void check_balance(){

        try {
            //Getting access token from table
            CustomerDAO customer = new CustomerDAO();
            customer.retrieveData(request.getPhonenumber());
            request.setUserToken(customer.getAccessToken());

            //POST request
            accessURL();

            //Evaluate response:
            //TODO error handling for failure response
            Gson g=new Gson();
            CheckBalanceResponse checkBalanceResponse= g.fromJson(responseData,CheckBalanceResponse.class);
            responseData=checkBalanceResponse.getBody().getFundsSufficient();


        } catch (Exception exception) {


        }




    }

    public void accessURL(){

        try{
            String bodyTemp = "{\"userToken\": \"" + request.getUserToken() + "\",\"totalAmount\": \"" + request.getTotalAmount() + "\","
                    + "\"mid\": \"" + request.getMid() + "\"}";

            String checksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(request.getMERCHANT_KEY(), bodyTemp);

            String body = "{\"head\":{\"clientId\":\"merchant-perpule-stg\",\"version\":\"v1\",\"requestTimestamp\":\"Time\","
                    + "\"channelId\":\"WEB\",\"signature\":\"" + checksum + "\"},\"body\":" + bodyTemp + "}";

            URL url = new URL("https://securegw-stage.paytm.in/paymentservices/pay/consult");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(body);
            wr.close();

            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response: " + responseData);
            }
            // System.out.append("Request: " + post_data + " ");
            responseReader.close();
            LOGGER.info("INFO:"+responseData);

        }catch(Exception e){}

    }

    public String getResponseData() {
        return responseData;
    }


}