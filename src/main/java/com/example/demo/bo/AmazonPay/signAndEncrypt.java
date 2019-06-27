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
            String sellerId = cred.getSellerId();
            String accessKey = cred.getAccessKey();
            String secretKey = cred.getSecretKey();

            HashMap<String, String> signAndEncryptParameters = new HashMap<String, String>();
            Enumeration<String> parameterNames = request.getParameterNames();

            while (parameterNames.hasMoreElements()) {

                String paramName = parameterNames.nextElement();

                String[] paramValue = request.getParameterValues(paramName);

                signAndEncryptParameters.put(paramName, paramValue[0]);


            }
            //TODO add entry to table properly

            AllocateTransactionId allocate=new AllocateTransactionId("9999999999",request.getParameter(PWAINConstants.ORDER_TOTAL_AMOUNT),new TransactionsDAO());

            signAndEncryptParameters.put(PWAINConstants.SELLER_ORDER_ID,allocate.Get_transactionID("amazonpay"));
            System.out.println(signAndEncryptParameters);
            PWAINBackendSDK backendSDK = new PWAINBackendSDK(new MerchantConfiguration.Builder().withAwsAccessKeyId(accessKey).withAwsSecretKeyId(secretKey).withSellerId(sellerId).build());

            return backendSDK.signAndEncryptParameters(signAndEncryptParameters);

        } catch (PWAINException e) {
            return e.toString();
        }
    }
}
