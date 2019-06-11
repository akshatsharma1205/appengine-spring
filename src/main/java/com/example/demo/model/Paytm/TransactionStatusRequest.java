package com.example.demo.model.Paytm;

public class TransactionStatusRequest {

    String merchantMid = "Delvit07224170213556";
    String orderId = "0";
    String merchantKey = "&!vj74@Ri&g6U1TI";

    public TransactionStatusRequest(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantMid() {
        return merchantMid;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMerchantKey() {
        return merchantKey;
    }
}
