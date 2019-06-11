package com.example.demo.model.Paytm;

public class ValidateTokenRequest {
    private String phonenumber="";
    private String sessionToken = "";

    public ValidateTokenRequest(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
