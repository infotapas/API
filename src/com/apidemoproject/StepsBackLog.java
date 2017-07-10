/**
 * 
 */
package com.apidemoproject;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.Validator;
import com.qmetry.qaf.automation.ws.rest.RestTestBase;
import com.qmetry.qaf.automation.ws.rest.RestWSTestCase;
import com.sun.jersey.api.client.ClientResponse;
import  static com.qmetry.qaf.automation.step.CommonStep.*;

/**
 * @author tapas.padhi
 *
 */
public class StepsBackLog extends RestWSTestCase {

	/**
	 * Auto-generated code snippet by QMetry Automation Framework.
	 */
	
	
	
	@QAFTestStep(description = "User create new order for {0} with amount {1}")
	public void addorder(String clientname, String amount) {

		String data = String.format("{'clientName':'%s','amount':'%s'}", clientname, amount);
		String baseUrl = ConfigurationManager.getBundle().getString("env.baseurl");
		getClient().resource(baseUrl+"/orders.json").post(data);

	}

	@QAFTestStep(description = "Order should be created with id say {0}")
	public static void it_should_have_orderid(String outOrderId) {
		String location = new RestTestBase().getResponse().getHeaders().getFirst("Location");
		assertThat("Order location", location, Matchers.notNullValue());
		ConfigurationManager.getBundle().setProperty(outOrderId,location.substring(location.lastIndexOf("/") + 1, location.lastIndexOf(".")));

	}

	@QAFTestStep(description = "Order exist with id {0}")
	public static void getOrder(String orderId) {
		
		String resource = String.format("/orders/%s.json", orderId);
	  requestForResource(resource);
		response_should_have_status("OK");
	}

	@QAFTestStep(description = "User deletes order with id {0}")
	public static void deleteOrder(String orderId) {
		String resource = String.format("/orders/%s.json", orderId);
		String baseUrl = ConfigurationManager.getBundle().getString("env.baseurl");
		
		RestWSTestCase r=new RestWSTestCase();
		r.getClient().resource(baseUrl+resource).delete();
	
	}

	@QAFTestStep(description = "Order Entry for {0} should be updated with amount {1} on Orders Page")
	public void editOrder(String orderId, String amount) throws Throwable {
		String data = String.format("{'amount':'%s'}", amount);
		String resource = String.format("/orders/%s/edit.json", orderId);
		String baseUrl = ConfigurationManager.getBundle().getString("env.baseurl");
		RestWSTestCase r=new RestWSTestCase();
		r.getClient().resource(baseUrl+resource).put(data);
	
	}

	/**
	 * Auto-generated code snippet by QMetry Automation Framework.
//	 */

		
		@QAFTestStep(description = "Validate order details with id as {0}, name as {1} and amount as {2}")
		 public void validateOrderDetail(String orderId, String clientName, String amount) {
		  String baseUrl = ConfigurationManager.getBundle().getString("env.baseurl");
		  String appendUrl = String.format("/orders/%s.json", orderId);
		  getClient().resource(baseUrl + appendUrl).get(ClientResponse.class);
		  if (verifyThat(getResponse().getStatus(), Matchers.equalTo(ClientResponse.Status.OK))) {
		   JSONObject jsonObject = new JSONObject(getResponse().getMessageBody());
		   Reporter.log(jsonObject.toString());
		   // Validate Order id
		   Validator.verifyThat("Validation of order id", jsonObject.get("id").toString(), Matchers.equalTo(orderId));
		   // Validate Client Name
		   verifyThat("Validation of client name", jsonObject.get("clientName").toString(), Matchers.equalTo(clientName));
		   // Validate Amount
		   verifyThat("Validation of amount", jsonObject.getInt("amount"), Matchers.equalTo(Integer.parseInt(amount)));
		  }
		 }

		
		@QAFTestStep(description ="Verify Order Entry for {0} should be updated with amount {1} on Orders Page")
		 public void validateupdatedDetails(String orderId, String amount) {
		  String baseUrl = ConfigurationManager.getBundle().getString("env.baseurl");
		  String appendUrl = String.format("/orders/%s.json", orderId);
		  getClient().resource(baseUrl + appendUrl).get(ClientResponse.class);
		  if (verifyThat(getResponse().getStatus(), Matchers.equalTo(ClientResponse.Status.OK))) {
		   JSONObject jsonObject = new JSONObject(getResponse().getMessageBody());
		   Reporter.log(jsonObject.toString());
		   // Validate Order id	   
		   Validator.verifyThat("Validation of order id", jsonObject.get("id").toString(), Matchers.equalTo(orderId));
		  //validate amount
		   verifyThat("Validation of amount", jsonObject.getInt("amount"), Matchers.equalTo(Integer.parseInt(amount)));
		  }
		 }
		
		@QAFTestStep(description = "Validate order details with id as {0}, name as {1} and amount as {2} not present")
		 public void validateOrderDetailnotpresent(String orderId, String clientName, String amount) {
		  String baseUrl = ConfigurationManager.getBundle().getString("env.baseurl");
		  getClient().resource(baseUrl + "/orders.json").get(ClientResponse.class);
		  if (verifyThat(getResponse().getStatus(), Matchers.equalTo(ClientResponse.Status.OK))) {
		   JSONArray jsonObject = new JSONArray(getResponse().getMessageBody());
		   for (Object object : jsonObject) {
			   
		    JSONObject h=(JSONObject)object;
		    
			   if (h.get("id").toString().equalsIgnoreCase(orderId)) {
				   Reporter.log("odred not deleted",MessageTypes.Fail);
			}
		
		 }
		   Reporter.log("order deleted",MessageTypes.Pass);
		
		
		  }
		  }	
	}

