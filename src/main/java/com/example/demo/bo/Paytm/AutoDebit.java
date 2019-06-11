package com.example.demo.bo.Paytm;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.DatabaseController;
import com.example.demo.dao.TransactionsDAO;
import com.example.demo.model.Paytm.AutoDebitRequest;
import com.example.demo.model.Paytm.TransactionStatusResponse;
import com.google.gson.Gson;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TreeMap;
import java.util.logging.Logger;

public class AutoDebit {
// for production
// URL transactionURL = new URL("https://securegw.paytm.in/order/directPay");

    private static final Logger LOGGER = Logger.getLogger(DatabaseController.class.getName());

    String responseData;
    private CustomerDAO customer;
    private TransactionsDAO transaction;

    AutoDebitRequest request;
    public String getResponseData() {
        return responseData;
    }

    public AutoDebit(String number,String amount,String orderID,CustomerDAO cust, TransactionsDAO trans) {
        this.request=new AutoDebitRequest(number,amount,orderID);
        this.customer=new CustomerDAO(cust);
        this.transaction=new TransactionsDAO(trans);
    }

    public void auto_debit() {


        try {
            //retrieve state for given phone number
            customer.retrieveData(request.getNumber());
            request.setAccessToken(customer.getAccessToken());
            request.setCustomerId(customer.getCustomerId());

            accesdURL();


            TransactionStatus transactionStatus = new TransactionStatus(request.getOrderId());
            transactionStatus.transaction_status();
            //TODO handle response
            LOGGER.info("T.Status"+responseData);



            Gson g=new Gson();
            TransactionStatusResponse transactionStatusResponse= g.fromJson(transactionStatus.getResponseData(),TransactionStatusResponse.class);
            responseData=transactionStatusResponse.getSTATUS();

            transaction.updateStatus(request.getOrderId(),responseData);


        } catch (Exception exception) {
                LOGGER.info("error");
            exception.printStackTrace();
        }
    }

    public void accesdURL(){


        TreeMap<String, String> paytmParams = new TreeMap<String, String>();

        paytmParams.put("ReqType", request.getReqType());
        // paytmParams.put("PREAUTH_ID", ""); // only in case of capture
        paytmParams.put("SSOToken", request.getAccessToken());
        paytmParams.put("MID", request.getMID());
        paytmParams.put("TxnAmount", request.getTxn());
        //TODO device ip address;
        paytmParams.put("AppIP", request.getAppIP());

        paytmParams.put("OrderId", request.getOrderId());
        paytmParams.put("Currency", request.getCurrency());
        paytmParams.put("DeviceId", request.getNumber());
        paytmParams.put("PaymentMode", request.getPaymentMode());
        paytmParams.put("CustId", request.getCustomerId());
        paytmParams.put("IndustryType", request.getIndustryType());
        paytmParams.put("Channel", request.getChannel());
        paytmParams.put("AuthMode", request.getAuthMode());

        try{
            URL transactionURL = new URL("https://securegw-stage.paytm.in/order/directPay");

            String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(request.getMerchantKey(), paytmParams);
            paytmParams.put("CheckSum", paytmChecksum);

            JSONObject obj = new JSONObject(paytmParams);
            String postData = obj.toString();
            HttpURLConnection connection = (HttpURLConnection) transactionURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
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

        }catch (Exception e){}

    }

}
