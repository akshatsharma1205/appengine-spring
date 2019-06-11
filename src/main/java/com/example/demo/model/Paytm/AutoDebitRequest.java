package com.example.demo.model.Paytm;

public class AutoDebitRequest {

    private String merchantKey = "&!vj74@Ri&g6U1TI";
    private String number;
    private String txn;
    private String accessToken;
    private String orderId;
    private String CustomerId;
    private String ReqType="WITHDRAW";
    private String MID="Delvit07224170213556";
    private String AppIP="100.108.170.13";
    private String Currency="INR";
    private String PaymentMode="PPI";
    private String IndustryType="Retail";
    private String Channel="WAP";
    private String AuthMode="USRPWD";

    public AutoDebitRequest() {
    }

    public AutoDebitRequest(String number, String txn, String orderId) {
        this.number = number;
        this.txn = txn;
        this.orderId = orderId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public String getReqType() {
        return ReqType;
    }

    public String getMID() {
        return MID;
    }

    public String getAppIP() {
        return AppIP;
    }

    public String getCurrency() {
        return Currency;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public String getIndustryType() {
        return IndustryType;
    }

    public String getChannel() {
        return Channel;
    }

    public String getAuthMode() {
        return AuthMode;
    }
}

