package com.example.demo.bo.AmazonPay;

import com.amazon.pwain.PWAINBackendSDK;
import com.amazon.pwain.PWAINException;
import com.amazon.pwain.types.MerchantConfiguration;
import com.amazon.pwain.types.PWAINConstants;
import com.example.demo.dao.TransactionsDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;

public class verifySignature {

    public static boolean verifySignature(HttpServletRequest request, HttpServletResponse response){

        try {
            MerchantCredentials cred = new MerchantCredentials();

            //Set the merchant credentials

            String sellerId = cred.getSellerId();
            String accessKey = cred.getAccessKey();
            String secretKey = cred.getSecretKey();

            //create a new hashmap in which we add all the required parameters 

            HashMap<String, String> verificationParameters = new HashMap<String, String>();

            //Retrieve the parameters name from the url 

            Enumeration<String> parameterNames = request.getParameterNames();


            //Add the parameters from the url in the hash map

            while (parameterNames.hasMoreElements()) {

                String paramName = parameterNames.nextElement();

                String[] paramValue = request.getParameterValues(paramName);

                verificationParameters.put(paramName, paramValue[0]);

            }

            //Set the connection with our merchant credentials 

            PWAINBackendSDK backendSDK = new PWAINBackendSDK(new MerchantConfiguration.Builder().withAwsAccessKeyId(accessKey).withAwsSecretKeyId(secretKey).withSellerId(sellerId).build());

            //Call the function verify signature with required parameters

            backendSDK.verifySignature(verificationParameters);

            //Checking if the verify signature contain transaction status, if yes then save the status in transaction table

            if(request.getParameterMap().containsKey(PWAINConstants.TRANSACTION_STATUS_DESCRIPTION)){
                System.out.println(request.getParameter(PWAINConstants.TRANSACTION_STATUS_DESCRIPTION));
                TransactionsDAO transaction = new TransactionsDAO();
                transaction.updateStatus(request.getParameter(PWAINConstants.MERCHANT_TRANSACTION_ID),request.getParameter(PWAINConstants.TRANSACTION_STATUS_DESCRIPTION) );
            }



            return true;
        } catch (PWAINException e){
            System.out.println("returning verification as false");
            return false;
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

    }
}
