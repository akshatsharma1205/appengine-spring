package com.example.demo.bo.AmazonPay;

import com.amazon.pwain.PWAINBackendSDK;
import com.amazon.pwain.PWAINException;
import com.amazon.pwain.types.MerchantConfiguration;
import com.amazon.pwain.types.PWAINConstants;
import com.example.demo.bo.AllocateTransactionId;
import com.example.demo.dao.TransactionsDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;

public class signAndEncrypt {

    public static String signNEncrypt(HttpServletRequest request, HttpServletResponse response){


        try {
            MerchantCredentials cred = new MerchantCredentials();

            //Set the merchant credentials

            String sellerId = cred.getSellerId();
            String accessKey = cred.getAccessKey();
            String secretKey = cred.getSecretKey();

            //create a new hashmap in which we add all the required parameters

            HashMap<String, String> signAndEncryptParameters = new HashMap<String, String>();

            //Retrieve the parameters name from the url 
            
            Enumeration<String> parameterNames = request.getParameterNames();

            //Add the parameters from the url in the hash map

            while (parameterNames.hasMoreElements()) {

                String paramName = parameterNames.nextElement();

                String[] paramValue = request.getParameterValues(paramName);

                signAndEncryptParameters.put(paramName, paramValue[0]);


            }
            //TODO add entry to table properly

            //Allocate transactionID for AmazonPay transaction
            //We need to pass two parameters to allocate transactionID
            // 1) Mobile number ( Which is set to "9999999999" for testing case in production we get this number from our OnePay application)
            // 2) Transaction Amount

            AllocateTransactionId allocate=new AllocateTransactionId( "9999999999",request.getParameter(PWAINConstants.ORDER_TOTAL_AMOUNT),new TransactionsDAO());

            //call allocate.Get_transactionID with "amazonpay" which will set mode to amazonpay

            signAndEncryptParameters.put(PWAINConstants.SELLER_ORDER_ID,allocate.Get_transactionID("amazonpay"));

            //Set the connection with our merchant credentials
            
            PWAINBackendSDK backendSDK = new PWAINBackendSDK(new MerchantConfiguration.Builder().withAwsAccessKeyId(accessKey).withAwsSecretKeyId(secretKey).withSellerId(sellerId).build());

            // Return the response recieved from calling signAndEncryptParameters
            
            return backendSDK.signAndEncryptParameters(signAndEncryptParameters);

        } catch (PWAINException e) {
            return e.toString();
        }
    }
}
