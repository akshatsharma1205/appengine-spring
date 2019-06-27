package com.example.demo.bo.PhonePe;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.demo.bo.PhonePe.SHA256.SHA256;

public class CheckPaymentStatus {
    public String getResponseData() {
        return responseData;
    }
    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
    public CheckPaymentStatus() { }
    String responseData;
    String merchantId = "M2306160483220675579140";
    String transactionId ;
    String salt_key="8289e078-be0b-484d-ae60-052f117f8deb";
    String salt_index="1";

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void checkpaymentstatus_main(){

        try {
            URL transactionURL = new URL("https://mercury-uat.phonepe.com/v3/transaction/"+merchantId+"/"+transactionId+"/status");

            String xverify = SHA256( "/v3/transaction/"+merchantId+"/"+transactionId+"/status" + salt_key) + "###" + salt_index;
            HttpURLConnection connection = (HttpURLConnection) transactionURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("x-verify",xverify);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));

            responseData = responseReader.readLine();
                    /*
            if (() != null) {
                System.out.append("Response Json = " + responseData);
            }
            System.out.append("Requested Json = " + xverify + " ^^^^^^^^^^^^^^^^");
            responseReader.close();

                     */


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}