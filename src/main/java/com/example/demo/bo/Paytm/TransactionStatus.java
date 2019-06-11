package com.example.demo.bo.Paytm;

import com.example.demo.model.Paytm.TransactionStatusRequest;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TreeMap;

public class TransactionStatus {

    private TransactionStatusRequest request;
    String responseData;

    public TransactionStatus() {
    }

    public TransactionStatus(String orderId) {
        this.request = new TransactionStatusRequest(orderId);
    }

    public String getResponseData() {
        return responseData;}

    public void transaction_status() {
        TreeMap<String, String> paytmParams = new TreeMap<String, String>();
        paytmParams.put("MID", request.getMerchantMid());
        paytmParams.put("ORDERID", request.getOrderId());

        try {
            URL transactionURL = new URL("https://securegw-stage.paytm.in/order/status");


            String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(request.getMerchantKey(), paytmParams);
            paytmParams.put("CHECKSUMHASH", paytmChecksum);
            JSONObject obj = new JSONObject(paytmParams);
            String postData = "JsonData=" + obj.toString();

            HttpURLConnection connection = (HttpURLConnection) transactionURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("contentType", "application/json");
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
            //return responseData;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
