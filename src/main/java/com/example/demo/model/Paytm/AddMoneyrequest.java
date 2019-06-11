package com.example.demo.model.Paytm;

public class AddMoneyrequest {

    String merchantMid = "Delvit07224170213556";
    // Key in your staging and production MID available in your dashboard
    String merchantKey = "&!vj74@Ri&g6U1TI";
    // Key in your staging and production merchant key available in your dashboard
    String orderId = "0";
    String sso_token = "0";
    String channelId = "WAP";
    String custId = "0";
    String mobileNo = "0";
    String email = "amba.rohit8@gmail.com";
    String txnAmount = "0";
    String website = "WEBSTAGING";
    // This is the staging value. Production value is available in your dashboard
    String industryTypeId = "Retail";
    // This is the staging value. Production value is available in your dashboard
    String callbackUrl = "https://micro-s-perpule.appspot.com/callbackurl";

    public AddMoneyrequest(String mobileNo, String txnAmount, String orderId) {
        this.mobileNo = mobileNo;
        this.txnAmount = txnAmount;
        this.orderId=orderId;
    }

    public void setSso_token(String sso_token) {
        this.sso_token = sso_token;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getMerchantMid() {
        return merchantMid;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSso_token() {
        return sso_token;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getCustId() {
        return custId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public String getTxnAmount() {
        return txnAmount;
    }

    public String getWebsite() {
        return website;
    }

    public String getIndustryTypeId() {
        return industryTypeId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
