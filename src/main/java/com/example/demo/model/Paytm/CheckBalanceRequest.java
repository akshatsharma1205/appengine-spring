package com.example.demo.model.Paytm;

public class CheckBalanceRequest {


    private String mid = "Delvit07224170213556";
    private String orderId = "0";
    private String userToken = "0";
    private String totalAmount = "0.00";
    private String MERCHANT_KEY = "&!vj74@Ri&g6U1TI";
    private String phonenumber="0";

    public CheckBalanceRequest(String phonenumber, String totalAmount, String orderId) {
        this.orderId = orderId;
        this.phonenumber = phonenumber;
        this.totalAmount = totalAmount;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getMid() {
        return mid;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getMERCHANT_KEY() {
        return MERCHANT_KEY;
    }

    public String getPhonenumber() {
        return phonenumber;
    }
}
