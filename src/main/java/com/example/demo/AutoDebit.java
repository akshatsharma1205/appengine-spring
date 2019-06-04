package com.example.demo;

import com.example.demo.Model.DatabaseController;
import com.google.gson.Gson;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TreeMap;
import java.util.logging.Logger;

public class AutoDebit {
// for production
// URL transactionURL = new URL("https://securegw.paytm.in/order/directPay");

    String merchantKey = "&!vj74@Ri&g6U1TI";
    String number;
    String txn;
    String accessToken;
    String orderId;
    String CustomerId;
    private static final Logger LOGGER = Logger.getLogger(DatabaseController.class.getName());


    public AutoDebit(String number, String txn) {
        this.number = number;
        this.txn = txn;
    }
    String responseData;

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }




public void auto_debit() {

    //retrieve state for given phone number
    try {
        Connection conn = DatabaseController.getConnection();
        PreparedStatement getstmt = conn.prepareStatement("SELECT * FROM Customer WHERE PhoneNumber LIKE ?");
        getstmt.setString(1, "%" + number + "%");
        ResultSet rs = getstmt.executeQuery();
        SQLTableEntry sl = new SQLTableEntry();
        sl.SQLRetrieve(rs);
        accessToken=sl.getAccessToken();
        CustomerId=sl.getCustomerId();

    }catch (Exception e){e.printStackTrace();}

    try {
        Connection conn = DatabaseController.getConnection();
        PreparedStatement getstmt = conn.prepareStatement("SELECT * FROM Transactions ORDER BY TransactionId DESC LIMIT 1");
        ResultSet rs = getstmt.executeQuery();
        SQLTransactions sl = new SQLTransactions();
        sl.SQLRetrieve(rs);
        orderId=sl.getTransactionId();
        orderId=Integer.toString(Integer.parseInt(orderId)+1);

    }catch (Exception e){e.printStackTrace();}

    LOGGER.info("bleh ");
    TreeMap<String, String> paytmParams = new TreeMap<String, String>();
    paytmParams.put("ReqType", "WITHDRAW");
// paytmParams.put("PREAUTH_ID", ""); // only in case of capture
    paytmParams.put("SSOToken", accessToken);
    paytmParams.put("MID", "Delvit07224170213556");
    paytmParams.put("TxnAmount", txn);
    //TODO device ip address;
    paytmParams.put("AppIP", "192.168.1.72");

    paytmParams.put("OrderId", orderId);
    paytmParams.put("Currency", "INR");
    paytmParams.put("DeviceId", number);
    paytmParams.put("PaymentMode", "PPI");
    paytmParams.put("CustId", CustomerId);
    paytmParams.put("IndustryType", "Retail");
    paytmParams.put("Channel", "WAP");
    paytmParams.put("AuthMode", "USRPWD");

    try {
        LOGGER.info("bleh bleh jhjjkfn");
        Connection conn = DatabaseController.getConnection();

        PreparedStatement setstmt = conn.prepareStatement(
                "INSERT INTO Transactions(TransactionId, PhoneNumber, Amount, Status) VALUES (?, ?,?,?);");
        setstmt.setString(1, orderId);
        setstmt.setString(2, number);
        setstmt.setString(3, txn);
        setstmt.setString(4, "Failure");
        setstmt.execute();

        LOGGER.info("dkcn"+setstmt);
    }catch(Exception e){}


    try {
        URL transactionURL = new URL("https://securegw-stage.paytm.in/order/directPay");

        LOGGER.info("bleh bleh");
        String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(merchantKey, paytmParams);
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


        //TODO handle response
        Gson g=new Gson();
        AutoDebitResponse autoDebitResponse= g.fromJson(responseData,AutoDebitResponse.class);
        responseData=autoDebitResponse.getStatus();

        Connection conn = DatabaseController.getConnection();
        PreparedStatement setstmt = conn.prepareStatement(
                "INSERT INTO Transactions(TransactionId, PhoneNumber, Amount, Status) VALUES (?, ?,?,?);");
        setstmt.setString(1, orderId);
        setstmt.setString(2, number);
        setstmt.setString(3, txn);
        setstmt.setString(4,responseData);
        setstmt.execute();


    } catch (Exception exception) {
        try {
            LOGGER.info("bleh bleh jhjjkfn");
            Connection conn = DatabaseController.getConnection();

            PreparedStatement setstmt = conn.prepareStatement(
                    "INSERT INTO Transactions(TransactionId, PhoneNumber, Amount, Status) VALUES (?, ?,?,?);");
            setstmt.setString(1, orderId);
            setstmt.setString(2, number);
            setstmt.setString(3, txn);
            setstmt.setString(4, "Failure");
            setstmt.execute();

            LOGGER.info("dkcn"+setstmt);
        }catch(Exception e){}
        exception.printStackTrace();
    }
}

}
