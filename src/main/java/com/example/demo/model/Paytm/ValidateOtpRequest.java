package com.example.demo.model.Paytm;

public class ValidateOtpRequest {

    private String phonenumber = "0";
    private String otp = "";
    private String state = "";
    //private String clientId = "merchant-perpule-stg";
    //private String clientSecret = "jlgBKygCagp6PKECMEbLBfCviJorleAj";

    public ValidateOtpRequest(String phonenumber, String otp) {
        this.phonenumber = phonenumber;
        this.otp = otp;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getOtp() {
        return otp;
    }

    public String getState() {
        return state;
    }

}
