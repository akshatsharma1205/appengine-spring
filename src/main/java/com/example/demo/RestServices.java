package com.example.demo;

import com.amazon.pwain.types.PWAINConstants;
import com.example.demo.bo.*;
import com.example.demo.bo.AmazonPay.signAndEncrypt;
import com.example.demo.bo.AmazonPay.signAndEncryptForOperation;
import com.example.demo.bo.AmazonPay.verifySignature;
import com.example.demo.bo.Paytm.*;
import com.example.demo.bo.PhonePe.*;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.DatabaseController;
import com.example.demo.dao.TransactionsDAO;
import com.example.demo.model.Paytm.SendOtpResponse;
import com.example.demo.model.Paytm.ValidateOtpResponse;

import com.example.demo.model.PhonePe.CheckPaymentStatusResponse;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;


@SpringBootApplication
@RestController

public class RestServices implements ErrorController {

	private static final Logger LOGGER = Logger.getLogger(DatabaseController.class.getName());

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
		return flow.CheckRegistered(number,totalamount,mode);
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


	//******************************************************************************************************************8
	//TODO phonepay exculsive

	@RequestMapping("/requestpayment")
	public String RequestPayment(@RequestParam(value ="number",defaultValue = "0")String number,
								 @RequestParam(value="totalamount", defaultValue="0.00") Integer totalamount) {
		RequestPayment requestPayment = new RequestPayment();


		String transactionId = new AllocateTransactionId(number,String.valueOf(totalamount/100),new TransactionsDAO()).Get_transactionID("phonpe");
		requestPayment.setTransactionId(transactionId);
		requestPayment.requestpayment_main(number,totalamount);


		Gson g=new Gson();
		RequestPaymentResponse requestPaymentResponse= g.fromJson(requestPayment.getResponseData(),RequestPaymentResponse.class);
		if (requestPaymentResponse.getSuccess()==true){

			return transactionId;
		}
		else {
			//TODO add failure code
			return null;}
	}

	@RequestMapping("/checkpaymentstatus")
	public String CheckPaymentStatus(@RequestParam(value="transid",defaultValue="0") String TransactionId) {

		CheckPaymentStatus checkPaymentStatus = new CheckPaymentStatus();
		checkPaymentStatus.setTransactionId(TransactionId);
		checkPaymentStatus.checkpaymentstatus_main();

		Gson g=new Gson();
		CheckPaymentStatusResponse checkPaymentStatusResponse= g.fromJson(checkPaymentStatus.getResponseData(), CheckPaymentStatusResponse.class);

		TransactionsDAO transaction = new TransactionsDAO();
		transaction.updateStatus(TransactionId, checkPaymentStatusResponse.getCode());

		return (checkPaymentStatusResponse.getMessage());

	}


	@RequestMapping("/cancelpaymentrequest")
	public String CancelPaymentRequest() {

		CancelPaymentRequest can = new CancelPaymentRequest();
		can.cancelpaymentrequest_main();
		return can.getResponseData();
	}


	@RequestMapping("/remindpaymentrequest")
	public String RemindPaymentRequest() {

		RemindPaymentRequest remind = new RemindPaymentRequest();
		remind.remindpaymentrequest_main();
		return remind.getResponseData();
	}
//******************************************************************************************************************
	//TODO AmazonPay exclusive

	// Amazon transaction hits the banckend four time in the following sequence
	// 1) signAndEncrypt
	// 2) verifySignature
	// 3) signAndEncryptForOperation
	// 4) verifySignature
	// We pass the same http request, parse it in java classes and return the response

	@GetMapping("/signAndEncrypt")
	public String signAndEncrypt(HttpServletRequest request, HttpServletResponse response){
		signAndEncrypt S = new signAndEncrypt();
		return S.signNEncrypt(request,response);
	}


	@GetMapping("/verifySignature")
	public Boolean verifySignature(HttpServletRequest request, HttpServletResponse response){
		verifySignature V = new verifySignature();
		return V.verifySignature(request,response);
	}


	@GetMapping("/signAndEncryptForOperation")
	public String signAndEncryptForOperation(HttpServletRequest request, HttpServletResponse response ){
		signAndEncryptForOperation S = new signAndEncryptForOperation();
		return S.signAndEncrypt(request,response);
	}


}
