package com.example.demo.bo.Paytm;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.TransactionsDAO;
import com.example.demo.model.Paytm.AddMoneyrequest;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;


public class AddMoney {


    private AddMoneyrequest addMoneyrequest;
    private String htmlResponse;
    private CustomerDAO customer;
    private TransactionsDAO transaction;

    public AddMoney(){}

    public AddMoney(String mobileNo, String txnAmount, String orderId, CustomerDAO cust, TransactionsDAO trans) {
        this.addMoneyrequest=new AddMoneyrequest(mobileNo, txnAmount, orderId);
        this.customer=new CustomerDAO(cust);
        this.transaction=new TransactionsDAO(trans);
    }

    public String getResponse() {
        return htmlResponse;
    }

    public void add_money() throws  Exception {

        try {

            //retrieve Customer Data
            customer.retrieveData(addMoneyrequest.getMobileNo());
            addMoneyrequest.setSso_token(customer.getAccessToken());
            addMoneyrequest.setCustId(customer.getCustomerId());

            //Update tranasactions table:
            transaction.updateStatus(addMoneyrequest.getOrderId(),"Adding_Money");

        }catch (Exception e){e.printStackTrace();}

        create_html_from();


    }

    public void create_html_from(){
        try {
            TreeMap<String, String> paytmParams = new TreeMap<String, String>();
            paytmParams.put("MID", addMoneyrequest.getMerchantMid());
            paytmParams.put("REQUEST_TYPE", "ADD_MONEY");
            paytmParams.put("SSO_TOKEN", addMoneyrequest.getSso_token());
            paytmParams.put("ORDER_ID", addMoneyrequest.getOrderId());
            paytmParams.put("CHANNEL_ID", addMoneyrequest.getChannelId());
            paytmParams.put("CUST_ID", addMoneyrequest.getCustId());
            paytmParams.put("MOBILE_NO", addMoneyrequest.getMobileNo());
            paytmParams.put("EMAIL", addMoneyrequest.getEmail());
            paytmParams.put("TXN_AMOUNT", addMoneyrequest.getTxnAmount());
            paytmParams.put("WEBSITE", addMoneyrequest.getWebsite());
            paytmParams.put("INDUSTRY_TYPE_ID", addMoneyrequest.getIndustryTypeId());
            paytmParams.put("CALLBACK_URL", addMoneyrequest.getCallbackUrl());
            String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(addMoneyrequest.getMerchantKey(), paytmParams);
            StringBuilder outputHtml = new StringBuilder();
            outputHtml.append("<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>");
            outputHtml.append("<html>");
            outputHtml.append("<head>");
            outputHtml.append("<title>Merchant Checkout Page</title>");
            outputHtml.append("</head>");
            outputHtml.append("<body>");
            outputHtml.append("<center><h1>Please do not refresh this page...</h1></center>");
            URL transactionURL = new URL("https://securegw-stage.paytm.in/theia/processTransaction");
            outputHtml.append("<form method='post' action='" + transactionURL + "' name='f1'>");
            for (Map.Entry<String, String> entry : paytmParams.entrySet()) {
                outputHtml.append("<input type='hidden' name='" + entry.getKey() + "' value='" + entry.getValue() + "'>");
            }
            outputHtml.append("<input type='hidden' name='CHECKSUMHASH' value='" + paytmChecksum + "'>");
            outputHtml.append("</form>");
            outputHtml.append("<script type='text/javascript'>");
            outputHtml.append("document.f1.submit();");
            outputHtml.append("</script>");
            outputHtml.append("</body>");
            outputHtml.append("</html>");
            htmlResponse = outputHtml.toString();
        }catch(Exception e){}

    }
}
