package com.example.demo.bo.AmazonPay;

import com.amazon.pwain.PWAINBackendSDK;
import com.amazon.pwain.PWAINException;
import com.amazon.pwain.types.MerchantConfiguration;
import com.amazon.pwain.types.PWAINConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class signAndEncryptForOperation {

    public static String signAndEncrypt(HttpServletRequest request, HttpServletResponse response){

        try {
            MerchantCredentials cred = new MerchantCredentials();

            String sellerId = cred.getSellerId();
            String accessKey = cred.getAccessKey();
            String secretKey = cred.getSecretKey();

            String amazonOrderId = request.getParameter(PWAINConstants.TRANSACTION_ID);
            String transactionType = request.getParameter(PWAINConstants.TRANSACTION_ID_TYPE);

            HashMap<String, String> getChargeParameters = new HashMap<String, String>();
            getChargeParameters.put(PWAINConstants.TRANSACTION_ID,amazonOrderId);
            getChargeParameters.put(PWAINConstants.TRANSACTION_ID_TYPE,transactionType);
            getChargeParameters.put(PWAINConstants.OPERATION_NAME,"SIGN_AND_ENCRYPT_GET_CHARGE_STATUS_REQUEST");
            PWAINBackendSDK backendSDK = new PWAINBackendSDK(new MerchantConfiguration.Builder().withAwsAccessKeyId(accessKey).withAwsSecretKeyId(secretKey).withSellerId(sellerId).build());

            return backendSDK.signAndEncryptParameters(getChargeParameters);
        } catch (PWAINException e){
            return  e.toString();
        } catch (Exception ex){
            ex.printStackTrace();
            return ex.toString();
        }
    }
}
