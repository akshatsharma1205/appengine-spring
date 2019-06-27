package com.example.demo.bo.AmazonPay;

import com.amazon.pwain.PWAINBackendSDK;
import com.amazon.pwain.PWAINException;
import com.amazon.pwain.types.MerchantConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;

public class verifySignature {

    public static boolean verifySignature(HttpServletRequest request, HttpServletResponse response){

        try {
            MerchantCredentials cred = new MerchantCredentials();

            String sellerId = cred.getSellerId();
            String accessKey = cred.getAccessKey();
            String secretKey = cred.getSecretKey();

            HashMap<String, String> verificationParameters = new HashMap<String, String>();

            Enumeration<String> parameterNames = request.getParameterNames();



            while (parameterNames.hasMoreElements()) {

                String paramName = parameterNames.nextElement();

                String[] paramValue = request.getParameterValues(paramName);

                verificationParameters.put(paramName, paramValue[0]);

            }

            //if(x!=null&&success==true)


            PWAINBackendSDK backendSDK = new PWAINBackendSDK(new MerchantConfiguration.Builder().withAwsAccessKeyId(accessKey).withAwsSecretKeyId(secretKey).withSellerId(sellerId).build());

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
