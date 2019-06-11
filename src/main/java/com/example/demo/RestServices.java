package com.example.demo;

import com.example.demo.bo.*;
import com.example.demo.bo.Paytm.*;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.TransactionsDAO;
import com.example.demo.model.Paytm.SendOtpResponse;
import com.example.demo.model.Paytm.ValidateOtpResponse;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;


@SpringBootApplication
@RestController

public class RestServices implements ErrorController {

	public static void main(String[] args) {
		SpringApplication.run(RestServices.class, args);
	}

    private final AtomicLong counter = new AtomicLong();
	SendOtpResponse otpresponse;

	//For a NULL Page
	@GetMapping("/")
	public String hello() {
		return "Visit https://www.perpule.com";
	}

	//Handling /error throwback
	@RequestMapping("/error")
	public String handleError() {
		//do something like logging
		return "We encountered an error. Lol, better get to someone with more knowledge!";
	}
	@Override
	public String getErrorPath() {
		return "/error";
	}


	//GENERALISED REST_API
	@RequestMapping("/checklinking")
	public String checkexistence(@RequestParam(value ="mode",defaultValue = "0")String mode,
								 @RequestParam(value ="number",defaultValue = "0")String number,
								 @RequestParam(value="totalamount", defaultValue="0.00") String totalamount){
		Flow flow;
		switch(mode) {
			case "paytm":
				flow = new PaytmFlow();
			default:
				flow = new PaytmFlow();
		}
		return flow.CheckRegistered(number,totalamount);
	}

	@RequestMapping("/initiate")
	public String initiate(@RequestParam(value ="mode",defaultValue = "0")String mode,
							@RequestParam(value ="number",defaultValue = "0")String number,
							@RequestParam(value="totalamount", defaultValue="0.00") String totalamount,
							@RequestParam(value="transid",defaultValue="0") String TransactionId){
		Flow flow;
		switch(mode) {
			case "paytm":
				flow = new PaytmFlow();
			default:
				flow = new PaytmFlow();
		}
		return flow.Inititate(number, totalamount, TransactionId);
	}

	@RequestMapping("/debit")
	public String debit(@RequestParam(value ="mode",defaultValue = "0")String mode,
							@RequestParam(value ="number",defaultValue = "0")String number,
							@RequestParam(value="totalamount", defaultValue="0.00") String totalamount,
							@RequestParam(value="transid",defaultValue="0") String TransactionId){
		Flow flow;
		switch(mode) {
			case "paytm":
				flow = new PaytmFlow();
			default:
				flow = new PaytmFlow();

		}
		return flow.Debit( number, totalamount, TransactionId);
	}




	//*************************************************************************************
	//PAYTM - Exclusive Rest APIs

	@RequestMapping("/sendotp")
	public SendOtpResponse sendingotp(@RequestParam(value="number", defaultValue="0") String number ) {
		SendOtp so = new SendOtp("", "+91" + number, new CustomerDAO());
		so.Send_OTP();
		Gson g=new Gson();
		otpresponse= g.fromJson(so.getResponseData(),SendOtpResponse.class);
		return otpresponse;

	}

	@RequestMapping("/validateotp")
	public String validatingotp(@RequestParam(value="number", defaultValue="0") String number,
								@RequestParam(value="otp", defaultValue="111111") String otp) {
		ValidateOtp votp =new ValidateOtp(number,otp,new CustomerDAO());
		votp.validate_OTP();
		return votp.getResponseData();
	}



	@RequestMapping("/addmoney")
	public String addmoney(@RequestParam(value="number", defaultValue="0") String number,
                           @RequestParam(value="topupamt", defaultValue="0.00") String topupamt,
                           @RequestParam(value="transid",defaultValue="0") String TransactionId){
		AddMoney addMoney = new AddMoney(number,topupamt,TransactionId, new CustomerDAO(), new TransactionsDAO());
		try{
			addMoney.add_money();
		}catch (Exception e){}

		return addMoney.getResponse();

	}

	@RequestMapping("/callbackurl")
    public String callback(){
	    return "Please press continue to go ahead !";
    }


    //TODO work on this
	ValidateOtpResponse validateOtpResponse;
	@RequestMapping("/revokeaccess")
	public String revokingaccess(){
		RevokeAccess revoke=new RevokeAccess(validateOtpResponse.getAccess_token());
		revoke.revoke_access();
		return revoke.getResponseData();
	}


}
