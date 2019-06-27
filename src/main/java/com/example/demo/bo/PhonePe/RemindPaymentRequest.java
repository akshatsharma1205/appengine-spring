package com.example.demo.bo.PhonePe;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.example.demo.bo.PhonePe.SHA256.SHA256;

public class RemindPaymentRequest {
    public String getResponseData() {
        return responseData;
    }
    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
    public RemindPaymentRequest() { }
    String responseData;
    String merchantId = "M2306160483220675579140";
    String transactionId = "TX123456967859qwwww";
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

    public void remindpaymentrequest_main(){

        try {
            URL transactionURL = new URL("https://mercury-uat.phonepe.com/v3/charge/"+merchantId+"/"+transactionId+"/remind");

            String xverify = SHA256( "/v3/charge/"+merchantId+"/"+transactionId+"/remind" + salt_key) + "###" + salt_index;
            HttpURLConnection connection = (HttpURLConnection) transactionURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("x-verify",xverify);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response Json = " + responseData);
            }
            System.out.append("Requested Json = " + xverify + " ");
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}