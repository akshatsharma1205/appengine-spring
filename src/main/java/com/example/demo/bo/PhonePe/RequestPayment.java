package com.example.demo.bo.PhonePe;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Random;
import java.util.TreeMap;

import static com.example.demo.bo.PhonePe.SHA256.SHA256;
public class RequestPayment {


    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public RequestPayment() { }

    String responseData;
    Random rand= new Random();
    String merchantId = "M2306160483220675579140 ";
    //String transactionId = "TX123"+rand.nextInt(4000);
    String transactionId;
    String merchantOrderId = "M123456789";
    Integer amount = 1;
    String instrumentType = "MOBILE";
    String instrumentReference = "7397430279";
    String message = "collect for XXX order";
    String email = "amitxxx75@gmail.com";
    Integer expiresIn = 180;
    String storeId = "store1";
    String terminalId = "terminal1";
    String salt_key="8289e078-be0b-484d-ae60-052f117f8deb";
    String salt_index="1";

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void requestpayment_main(String number, Integer totalamount){

            TreeMap payload = new TreeMap();
            payload.put("merchantId", merchantId);
            payload.put("transactionId", transactionId);
            payload.put("merchantOrderId", merchantOrderId);
            payload.put("amount", totalamount);
            payload.put("instrumentType", instrumentType);
            payload.put("instrumentReference", number);
            payload.put("message", message);
            payload.put("email", email);
            payload.put("expiresIn", expiresIn);
            payload.put("storeId", storeId);
            payload.put("terminalId", terminalId);

            TreeMap body = new TreeMap();

        try {
            URL transactionURL = new URL("https://mercury-uat.phonepe.com/v3/charge");
            JSONObject obj = new JSONObject(payload);
            String auth = obj.toString();
            String postData = Base64.getEncoder().encodeToString((auth).getBytes());
            String xverify = SHA256(postData+ "/v3/charge" + salt_key) + "###" + salt_index;

            body.put("request", postData);
            JSONObject obj1 = new JSONObject(body);
            String auth1 = obj1.toString();

            HttpURLConnection connection = (HttpURLConnection) transactionURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("x-verify",xverify);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(auth1);
            requestWriter.close();

            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response Json = " + responseData);
            }
            System.out.append("Requested Json = " + xverify + " ");
            System.out.append("\nRequested Json = " + postData + " ");
            responseReader.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }
}

