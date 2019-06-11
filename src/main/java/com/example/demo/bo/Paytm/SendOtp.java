package com.example.demo.bo.Paytm;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.DatabaseController;
import com.example.demo.model.Paytm.SendOtpRequest;
import com.example.demo.model.Paytm.SendOtpResponse;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TreeMap;
import java.util.logging.Logger;

public class SendOtp {
	

	private static final Logger LOGGER = Logger.getLogger(DatabaseController.class.getName());
	private String responseData;
	private SendOtpRequest request;
	private CustomerDAO customer;


	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public SendOtp(String email, String phone, CustomerDAO cust) {
		this.request=new SendOtpRequest(email, phone);
		this.customer=new CustomerDAO(cust);
	}
	
	public void Send_OTP() {
		try {
			//POST request to Paytm send OTP
			accessURL();

			//Saving state from response received for particular phone number
			customer.retrieveData(request.getPhone());
			Gson g = new Gson();
			String state = g.fromJson(responseData, SendOtpResponse.class).getState();
			
			//INSERTING ONLY IF NUMBER EXIST / ACCESS TOKEN ALREADY NOT AVAILABLE
			if (customer.getAccessToken()==null) {
				if (customer.getPhoneNumber()==null) {
					customer.insertData(request.getPhone(),null,state,null,null);
				}
				else{
					customer.updateData(request.getPhone(),null,state,null,null);
				}
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void accessURL(){

		TreeMap<String, String> paytmParams = new TreeMap<String, String>();
		paytmParams.put("email", request.getEmail());
		paytmParams.put("phone", request.getPhone());
		paytmParams.put("clientId", request.getClientId());
		paytmParams.put("scope", request.getScope());
		paytmParams.put("responseType", request.getResponseType());

		try{
			URL transactionURL = new URL("https://accounts-uat.paytm.com/signin/otp");

			JSONObject obj = new JSONObject(paytmParams);
			String postData = obj.toString();

			HttpURLConnection connection = (HttpURLConnection) transactionURL.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(postData);
			requestWriter.close();

			InputStream is = connection.getInputStream();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
			if ((responseData = responseReader.readLine()) != null) {
				System.out.append("Response Json = " + responseData);
			}
			System.out.append("Requested Json = " + postData + " ");
			responseReader.close();
		}catch (Exception e){}


	}
}
