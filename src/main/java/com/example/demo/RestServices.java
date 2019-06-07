package com.example.demo;

import com.example.demo.bo.*;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.*;
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

	//To check existence of a number(customer in database)
	@RequestMapping("/checkexistence")
	public String checkexistence(@RequestParam(value ="number",defaultValue = "0")String number){
		try {

			CustomerDAO customer= new CustomerDAO();
			customer.retrieveData(number);

			if(customer.getAccessToken()==null){return "no";}
			else {return "yes";}

		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}


	@RequestMapping("/sendotp")
	public SendOtpResponse sendingotp(@RequestParam(value="number", defaultValue="0") String number ) {
		SendOtp so = new SendOtp("", "+91" + number);
		so.Send_OTP();
		Gson g=new Gson();
		otpresponse= g.fromJson(so.getResponseData(),SendOtpResponse.class);
		return otpresponse;

	}

    ValidateOtpResponse validateOtpResponse;
	@RequestMapping("/validateotp")
	public String validatingotp(@RequestParam(value="number", defaultValue="0") String number,@RequestParam(value="otp", defaultValue="111111") String otp) {
		ValidateOtp votp =new ValidateOtp(number,otp);
		votp.validate_OTP();
		return votp.getResponseData();
	}


	ValidateTokenResponse validateTokenResponse;
	AutoDebitResponse autoDebitResponse;

	@RequestMapping("/validatetoken")
	public String validatingtoken(@RequestParam(value="number", defaultValue="0") String number){
		ValidateToken token=new ValidateToken(number);
		token.validate_token();
		return token.getResponseData();
	}

    @RequestMapping("/gettransactionid")
    public String gettingId(@RequestParam(value="number", defaultValue="0") String number,
                            @RequestParam(value="totalamount", defaultValue="0.00") String totalamount){
        GetTransactionId getTransactionId=new GetTransactionId(number,totalamount);
        getTransactionId.Get_transactionID();
        return getTransactionId.getTransactionId();
    }

	@RequestMapping("/checkbalance")
	public String checkingbalance(@RequestParam(value="number", defaultValue="0") String number,
                                  @RequestParam(value="totalamount", defaultValue="0.00") String totalamount,
                                  @RequestParam(value="transid",defaultValue="0") String TransactionId){
		CheckBalance check=new CheckBalance(number,totalamount,TransactionId);
		check.check_balance();
		return check.getResponseData();
	}

	@RequestMapping("/addmoney")
	public String addmoney(@RequestParam(value="number", defaultValue="0") String number,
                           @RequestParam(value="topupamt", defaultValue="0.00") String topupamt,
                           @RequestParam(value="transid",defaultValue="0") String TransactionId){
		AddMoney addMoney = new AddMoney(number,topupamt,TransactionId);
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
	@RequestMapping("/revokeaccess")
	public String revokingaccess(){
		RevokeAccess revoke=new RevokeAccess(validateOtpResponse.getAccess_token());
		revoke.revoke_access();
		return revoke.getResponseData();
	}

	@RequestMapping("/autodebit")
	public String debiting(@RequestParam(value="number", defaultValue="0") String number,
                           @RequestParam(value="totalamount", defaultValue="0.00") String amount,
                           @RequestParam(value="transid",defaultValue="0") String TransactionId) {
		AutoDebit deb =new AutoDebit(number,amount,TransactionId);
		deb.auto_debit();
		return deb.getResponseData();
	}

	//TODO Do we need this API??
	@RequestMapping("/transactionstatus")
	public String txnstatus(){
		TransactionStatus stat=new TransactionStatus();
		stat.transaction_status();
		return stat.getResponseData();
	}
}
