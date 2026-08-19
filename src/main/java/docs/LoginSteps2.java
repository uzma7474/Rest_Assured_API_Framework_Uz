package docs;

import java.util.Map;

import org.testng.Assert;

import constants.HttpConstants;
import context.ScenarioContext;

import endpoints.AuthEndpoints;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.requests.RegisterRequest;
import models.response.BaseErrorResponse;
import models.response.LoginErrorResponse;
import models.response.LoginResponse;
import models.response.RegisterErrorResponse;
import models.response.RegisterResponse;
import models.response.UserResponse;
import services.LoginService;
import services.RegisterService;
import utils.TokenManager;

public class LoginSteps2 {
	
	//private TestContext context;
	
	// Static response variable or shared context can be used for responses
    public static Response sharedResponse; 
	
	private String email;
	private String password;

	private String registrationToken;
	private int userId;

    private Response authenticatedResponse;
    private String jwtToken;
    
    private String acceptHeader;

    private String contentTypeHeader;
	
    private RequestSpecification request;
    
	public RegisterRequest registerRequest;

	public LoginService loginService = new LoginService();
	public LoginResponse loginResponse;
	
	public LoginErrorResponse loginErrorResponse;

	private Response response;
	
	public UserResponse userResponse;
	
	private ScenarioContext context;
	
    // PicoContainer automatically injects this
    public LoginSteps2(ScenarioContext context) {
        this.context = context;
    }
	
	// ============================================================
	// PRINT RESPONSE
	// ============================================================

	private void printResponse() {

		System.out.println();
		System.out.println("========================================");
		System.out.println("Login API RESPONSE");
		System.out.println("========================================");

		// --------------------------------------------------------
		// Common Response Information
		// --------------------------------------------------------

		if (response != null) {

			System.out.println("Status Code : " + response.statusCode());

			System.out.println("Response Body:");

			System.out.println(response.asPrettyString());
		}

		// --------------------------------------------------------
		// SUCCESS RESPONSE
		// --------------------------------------------------------

		if (loginResponse != null) {

			System.out.println("Success : " + loginResponse.isSuccess());

			System.out.println("Token : " + loginResponse.getToken());

			if (loginResponse.getUser() != null) {

				System.out.println("User ID : " + loginResponse.getUser().getId());

				System.out.println("User Email : " + loginResponse.getUser().getEmail());
			}
		}

		// --------------------------------------------------------
		// ERROR RESPONSE
		// --------------------------------------------------------

		if (loginErrorResponse != null) {

			System.out.println("Success : " + loginErrorResponse.isSuccess());

			System.out.println("Error : " + loginErrorResponse.getError());

			
		}

		System.out.println("========================================");
		System.out.println();
	}

//======================================  GIVEN  ==========================================================
	
	@Given("the login API endpoint is {string}")
	public void the_login_API_endpoint_is(String logEndpoint) {
		Assert.assertEquals(AuthEndpoints.LOGIN, logEndpoint);
	}
	
	@Given("the request header {string} is set to {string}")
	public void the_request_header_Accept_is_set_to(String headerName, String headerValue) {
	      if ("Accept".equalsIgnoreCase(headerName)) {

	            this.acceptHeader = headerValue;

	        } else if ("Content-Type".equalsIgnoreCase(headerName)) {

	            this.contentTypeHeader = headerValue;
	        }
	}
	
	
	@Given("the login request contains:")
	public void the_login_request_contains(DataTable dataTable) {
		Map<String, String> data = dataTable.asMaps(String.class, String.class).get(0);

		registerRequest = new RegisterRequest(data.get("email"), data.get("password"));

		System.out.println("========================================");
		System.out.println("Registration Request");
		System.out.println("Email : " + data.get("email"));
		System.out.println("Password : " + data.get("password"));
		System.out.println("========================================");
		
	}
	
	
//======================================  WHEN  ==========================================================
	
	@When("the user sends a {string} request")
	public void the_user_sends_a_httpMethod_request(String httpMethod) {
		switch (httpMethod.toUpperCase()) {

		case "POST":

			// ---------------------------------------------
			// FIRST SEND THE REQUEST
			// ---------------------------------------------

			response = loginService.loginUser(registerRequest);

			// ---------------------------------------------
			// THEN CHECK THE RESPONSE STATUS
			// ---------------------------------------------

			if (response.statusCode() == HttpConstants.OK) {

				loginResponse = loginService.loginUserUsingPojo(response);

				System.out.println("Token : " + loginResponse.getToken());
				System.out.println("Success : " + loginResponse.isSuccess());
				System.out.println("User Id : " + loginResponse.getUser().getId());
				System.out.println("User email : " + loginResponse.getUser().getEmail());	
				
				// Store response and token in the shared context
		        //context.setResponse(response);
		        			
				// -----------------------------------------
				// SAVE TOKEN ONLY AFTER SUCCESSFUL RESPONSE
				// -----------------------------------------

		        String token = response.jsonPath().getString("token");
		        //context.setToken(token);
		        
		        // Store it securely in your ThreadLocal TokenManager
		        
				if (token != null && !token.isBlank()) {

					TokenManager.setToken(token);

					System.out.println("Token saved in TokenManager");
				}

			} else {

				loginErrorResponse = loginService.loginUserUsingPojo_failure(response);

				System.out.println("Error : " + loginErrorResponse.getError());
			}

			break;
			
		case "GET":
			// ---------------------------------------------
			// FIRST SEND THE REQUEST
			// ---------------------------------------------

			response = loginService.loginUser(registerRequest);

			// ---------------------------------------------
			// THEN CHECK THE RESPONSE STATUS
			// ---------------------------------------------

			if (response.statusCode() == HttpConstants.OK) {

				loginResponse = loginService.loginUserUsingPojo(response);

				System.out.println("Token : " + loginResponse.getToken());
				System.out.println("Success : " + loginResponse.isSuccess());
				System.out.println("User Id : " + loginResponse.getUser().getId());
				System.out.println("User email : " + loginResponse.getUser().getEmail());		
							
				// -----------------------------------------
				// SAVE TOKEN ONLY AFTER SUCCESSFUL RESPONSE
				// -----------------------------------------

				String token = response.jsonPath().getString("token");

				if (token != null && !token.isBlank()) {

						TokenManager.setToken(token);

						System.out.println("Token saved in TokenManager");
					}
			} else {

				loginErrorResponse = loginService.loginUserUsingPojo_failure(response);

				System.out.println("Error : " + loginErrorResponse.getError());
			}
			break;
			
		case "DELETE":
			// ---------------------------------------------
			// FIRST SEND THE REQUEST
			// ---------------------------------------------

			response = loginService.loginUser(registerRequest);

			// ---------------------------------------------
			// THEN CHECK THE RESPONSE STATUS
			// ---------------------------------------------

			if (response.statusCode() == HttpConstants.OK) {

				loginResponse = loginService.loginUserUsingPojo(response);

				System.out.println("Token : " + loginResponse.getToken());
				System.out.println("Success : " + loginResponse.isSuccess());
				System.out.println("User Id : " + loginResponse.getUser().getId());
				System.out.println("User email : " + loginResponse.getUser().getEmail());		
							
				// -----------------------------------------
				// SAVE TOKEN ONLY AFTER SUCCESSFUL RESPONSE
				// -----------------------------------------

				String token = response.jsonPath().getString("token");

				if (token != null && !token.isBlank()) {

						TokenManager.setToken(token);

						System.out.println("Token saved in TokenManager");
					}
			} else {

				loginErrorResponse = loginService.loginUserUsingPojo_failure(response);

				System.out.println("Error : " + loginErrorResponse.getError());
			}
			break;
			
		default:

			throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
		}

		System.out.println("HTTP Method : " + httpMethod);

		System.out.println("Status Code : " + response.statusCode());

		System.out.println("Response Body:");
		System.out.println(response.asPrettyString());

		printResponse();
	}
	
//======================================  THEN  ==========================================================
		
	// =========================================================
	// THEN - STATUS CODE
	// =========================================================
	
	//@Then("the response status code should be {int}")
	@Then("the login response status code should be {int}")
	public void the_registration_response_status_code_should_be(Integer expectedStatusCode) {

		Assert.assertEquals(response.statusCode(), expectedStatusCode.intValue(), "Unexpected response status code");
	}
	
	@Then("the response success should be {string}")
	public void response_of_success_field(String expectedSuccess) {
		boolean expected = Boolean.parseBoolean(expectedSuccess);
		boolean actual = response.jsonPath().getBoolean("success");
		System.out.println("Expected Success : " + expected);
		System.out.println("Actual Success : " + actual);
		//Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success to be true");
		Assert.assertEquals(actual, expected);
	}
	
	@Then("the login response should contain a non-empty token")
	public void response_should_contain_a_non_empty_token() {
		String token = response.jsonPath().getString("token");
		System.out.println("Token : " + token);
		Assert.assertNotNull(token, "Token is null");
		Assert.assertNotNull(token, "JWT Auth token was null in the response.");
	    Assert.assertFalse(token.trim().isEmpty(), "JWT Auth token was returned empty.");
	}
	
	@Then("the response should contain a {string} object")
	public void response_should_contain_a_user_object(String user) {
		userResponse = loginResponse.getUser();
		
		Assert.assertNotNull(userResponse, "User object is null & response doesn't contain user object");
	}
	
	@Then("the response user login email should be {string}")
	public void response_user_email_should_be(String expectedEmail) {
		email = loginResponse.getUser().getEmail();
		Assert.assertEquals(email, expectedEmail);
		
	}
	
	
	@Then("the response contain accept is {string}")
	public void response_contain_accept_is(String expectedContentType) {
		// application/json; charset=utf-8
		String actual = response.getContentType();
		// Split by semicolon and extract the first element, then remove extra spaces
		String actualContentType = contentTypeHeader.split(";")[0].trim();
		System.out.println("\nContentType : "+actualContentType);
		Assert.assertEquals(actualContentType, expectedContentType);
		System.out.println("\n============================================================\n");
	}
	
	@Then("the response user login user id {int}")
	public void response_user_login_user_id(Integer user_id) {
		// 2. Extract the nested integer field
		int actualUserId = response.jsonPath().getInt("user.id");

		System.out.println("Extracted User ID: " + actualUserId);
		Assert.assertEquals(actualUserId, user_id, "User ID verification failed!");
		
	}
	
	@Then("the details object in response is null or empty")
	public void details_object_is_empty_or_null() {
		/*
		 * Status Code : 400
			Response Body:
			{
			    "success": false,
			    "error": "Invalid email or password",
			    "details": [
			        
			    ]
			    
			}
		 */
		Assert.assertTrue(loginErrorResponse.getDetails().isEmpty(), "Detail object is not empty");
		//Assert.assertTrue(loginErrorResponse.getDetails(), "Detail object is not empty");
		
	}
	
	@Then("the details object field message should be {string}")
	public void detail_object_error_msg(String expectedError) {
		String actualError = loginErrorResponse.getDetails().get(0).getMessage();
		String detailField = loginErrorResponse.getDetails().get(0).getField();
		Assert.assertEquals(detailField, "email");
		Assert.assertEquals(actualError, expectedError);
	}
	
	@Then("the details object {string} message should be {string}")
	public void detail_object_error_should_be(String field, String expectedError) {
			    
	    // Check if the details array has items before processing
	    if (loginErrorResponse.getDetails() != null && !loginErrorResponse.getDetails().isEmpty()) {
	        
	        System.out.println("\n=================== Validation Details ===================\n");
	        Assert.assertEquals(loginErrorResponse.getDetails().get(0).getField(), field);
	    }
	}
	
	@Then("the response field {string} should be {string}")
	public void response_error_should_be(String responseField, String expectedErrorMsg) {
	    
	    // CASE 1: Validating the top-level "error" key
	    if ("error".equalsIgnoreCase(responseField)) {
	        String actualError = loginErrorResponse.getError();
	        Assert.assertEquals(actualError, expectedErrorMsg, "Main error message mismatch!");
	        return; // Exit out of the method early since validation is complete
	    }
	    
	    // CASE 2: Validating nested fields like "email" or "password" within the details array
	    boolean targetFieldFound = false;
	    
	    if (loginErrorResponse.getDetails() != null && !loginErrorResponse.getDetails().isEmpty()) {
	        
	        for (int i = 0; i < loginErrorResponse.getDetails().size(); i++) {
	            String targetField = loginErrorResponse.getDetails().get(i).getField();
	            String targetMessage = loginErrorResponse.getDetails().get(i).getMessage();
	            
	            // Check if the current object in the list matches the step's requested field parameter
	            if (targetField.equalsIgnoreCase(responseField)) {
	                System.out.println("\n[MATCH FOUND] Field: " + targetField + " | Expected Message: " + expectedErrorMsg);
	                Assert.assertEquals(targetMessage, expectedErrorMsg, "Validation message mismatch for field: " + responseField);
	                targetFieldFound = true;
	                break; // Break loop early since we found the field we were searching for
	            }
	        }
	    }
	    
	    // Ensure the scenario doesn't pass silently if the server completely omitted the requested field
	    Assert.assertTrue(targetFieldFound, "Expected validation field '" + responseField + "' was not found inside response details.");
	}

	
	
	
	
	
	
	

}
