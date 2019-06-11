package com.example.demo.model.Paytm;

public class SendOtpRequest {
    private String email = "";
    private String phone = "";
    private String clientId = "merchant-perpule-stg";
    private String scope = "wallet";
    private String responseType = "token";
    private String merchantKey = "&!vj74@Ri&g6U1TI";

    public SendOtpRequest(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getClientId() {
        return clientId;
    }

    public String getScope() {
        return scope;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getMerchantKey() {
        return merchantKey;
    }
}
