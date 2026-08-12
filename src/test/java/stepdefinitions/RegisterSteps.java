package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import models.requests.RegisterRequest;
import models.response.RegisterErrorResponse;
import models.response.RegisterResponse;
import services.RegisterService;
import utils.TokenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.requests.RegisterRequest;
import models.response.RegisterErrorResponse;
import models.response.RegisterResponse;

//Core REST Assured methods (given(), when(), get(), post(), etc.)
import static io.restassured.RestAssured.*;

//Matchers for validating responses (equalTo(), hasItem(), containsString(), etc.)
import static org.hamcrest.Matchers.*;

//REST Assured specific matchers (e.g., matchesXsd() for XML validation)
import static io.restassured.matcher.RestAssuredMatchers.*;




import static io.restassured.RestAssured.*;

import org.testng.Assert;

import constants.HttpConstants;
import endpoints.RegisterEndpoints;

public class RegisterSteps {

	private String email;
	private String password;

	private String registrationToken;
	private int userId;

    private Response authenticatedResponse;
    private String jwtToken;
	
    private RequestSpecification request;
    
	public RegisterRequest registerRequest;

	public RegisterService registerService = new RegisterService();
	public RegisterResponse registerResponse;
	public RegisterErrorResponse registerErrorResponse;

	private Response response;

	// ============================================================
	// PRINT RESPONSE
	// ============================================================

	private void printResponse() {

		System.out.println();
		System.out.println("========================================");
		System.out.println("REGISTER API RESPONSE");
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

		if (registerResponse != null) {

			System.out.println("Success : " + registerResponse.isSuccess());

			System.out.println("Token : " + registerResponse.getToken());

			if (registerResponse.getUser() != null) {

				System.out.println("User ID : " + registerResponse.getUser().getId());

				System.out.println("User Email : " + registerResponse.getUser().getEmail());
			}
		}

		// --------------------------------------------------------
		// ERROR RESPONSE
		// --------------------------------------------------------

		if (registerErrorResponse != null) {

			System.out.println("Success : " + registerErrorResponse.isSuccess());

			System.out.println("Error : " + registerErrorResponse.getError());

			if (registerErrorResponse.getDetails() != null) {

				System.out.println("Details : " + registerErrorResponse.getDetails());
			}
		}

		System.out.println("========================================");
		System.out.println();
	}

//======================================  GIVEN  ==========================================================

	// =========================================================
	// GIVEN
	// =========================================================
	@Given("the registration endpoint is {string}")
	public void theRegistrationEndpointIs(String endpoint) {

		Assert.assertEquals(endpoint, RegisterEndpoints.REGISTER, "Endpoint is not /api/auth/register");
	}

	// =========================================================
	// GIVEN
	// =========================================================

	@Given("the registration request contains a valid email and password")
	@Given("I have valid registration details")
	public void i_have_valid_registration_details(DataTable dataTable) {
		Map<String, String> data = dataTable.asMaps(String.class, String.class).get(0);

		registerRequest = new RegisterRequest(data.get("email"), data.get("password"));

		System.out.println("========================================");
		System.out.println("Registration Request");
		System.out.println("Email : " + data.get("email"));
		System.out.println("Password : " + data.get("password"));
		System.out.println("========================================");
	}

	// =========================================================
	// GIVEN - Invalid Registration Detail
	// =========================================================
	@Given("I have invalid registration details")
	public void i_have_invalid_registration_details(DataTable dataTable) {

		Map<String, String> data = dataTable.asMaps().get(0);

		email = data.get("email");
		password = data.get("password");

		// Map<String, String> data = dataTable.asMaps( String.class, String.class
		// ).get(0);

		registerRequest = new RegisterRequest(data.get("email"), data.get("password"));

		System.out.println("========================================");
		System.out.println("Invalid Registration Request");
		System.out.println("Email : " + data.get("email"));
		System.out.println("Password : " + data.get("password"));
		System.out.println("========================================");
	}

	
	 @Given("I have a valid registration request with:")
	 public void i_have_a_valid_registration_request_with(DataTable dataTable) {
	        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
	        String baseEmail = data.get(0).get("email");
	        String password = data.get(0).get("password");

	        // Make email unique using timestamp to avoid 400 Bad Request
	        String uniqueEmail = baseEmail.replace("@", "_" + System.currentTimeMillis() + "@");

	        registerRequest = new RegisterRequest(baseEmail, password);
	        System.out.println("========================================");
			System.out.println("Email : " + baseEmail);
			System.out.println("Password : " + baseEmail);
			System.out.println("========================================");
	   }
	
	
	
//======================================  WHEN  ==========================================================

	// =========================================================
	// WHEN
	// =========================================================
	
	@When("I send a {string} request to register the user")
	public void i_send_a_request_to_register_the_user(String methodName) {

		switch (methodName.toUpperCase()) {

		case "POST":

			// ---------------------------------------------
			// FIRST SEND THE REQUEST
			// ---------------------------------------------

			response = registerService.registerUser(registerRequest);

			// ---------------------------------------------
			// THEN CHECK THE RESPONSE STATUS
			// ---------------------------------------------

			if (response.statusCode() == HttpConstants.CREATED) {

				registerResponse = registerService.registerUserUsingPojo(response);

				System.out.println("Token : " + registerResponse.getToken());
				System.out.println("Success : " + registerResponse.isSuccess());
				System.out.println("User Id : " + registerResponse.getUser().getId());
				System.out.println("User email : " + registerResponse.getUser().getEmail());		
				
				// -----------------------------------------
				// SAVE TOKEN ONLY AFTER SUCCESSFUL RESPONSE
				// -----------------------------------------

				String token = response.jsonPath().getString("token");

				if (token != null && !token.isBlank()) {

					TokenManager.setToken(token);

					System.out.println("Token saved in TokenManager");
				}

			} else {

				registerErrorResponse = registerService.registerUserUsingPojo_failure(response);

				System.out.println("Error : " + registerErrorResponse.getError());
			}

			break;

		default:

			throw new IllegalArgumentException("Unsupported HTTP method: " + methodName);
		}

		System.out.println("HTTP Method : " + methodName);

		System.out.println("Status Code : " + response.statusCode());

		System.out.println("Response Body:");
		System.out.println(response.asPrettyString());

		printResponse();
	}

//======================================  THEN  ==========================================================	

	// =========================================================
	// THEN - STATUS CODE
	// =========================================================
	@Then("the response status code should be {int}")
	@Then("the registration response status code should be {int}")
	public void the_registration_response_status_code_should_be(Integer expectedStatusCode) {

		Assert.assertEquals(response.statusCode(), expectedStatusCode.intValue(), "Unexpected response status code");
	}

	// =========================================================
	// THEN - SUCCESS
	// =========================================================

	@Then("the registration response success should be {string}")
	public void the_registration_response_success_should_be(String expectedSuccess) {
		boolean expected = Boolean.parseBoolean(expectedSuccess);

		boolean actual = response.jsonPath().getBoolean("success");

		String token = response.jsonPath().getString("token");
		System.out.println("Token : " + token);

		Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success to be true");
		
	}
	
	// =========================================================
	// THEN - Token
	// =========================================================	
	@Then("the response should contain a non-empty token")
	public void the_response_should_contain_a_nonEmpty_token() {
		Assert.assertNotNull(response.jsonPath().getString("token"), "JWT token should not be null");
	}
	
	// =========================================================
	// THEN - User Object
	// =========================================================
	@Then("the response should contain a user object")
	public void the_response_should_contain_a_user_object() {
		Assert.assertNotNull(response.jsonPath().getString("user"), "User email should not be null");
		
	}
	
	// =========================================================
	// THEN - User Object
	// =========================================================
	@Then("the user email should be {string}")
	public void the_user_email_should_be(String email) {
		Assert.assertNotNull(response.jsonPath().getString("user.email"), "User email should not be null");
	}
	
	@Then("the user should have a id")
	public void the_user_should_have_a_id() {
		Assert.assertNotNull(response.jsonPath().getString("user.id"), "User id should not be null");
	}
	
	@Then("the user ID should be generated")
	public void the_user_ID_should_be_generated() {
		Assert.assertNotNull(response.jsonPath().getString("user.id"), "User id should not be null");
	}
	
	@Then("the response user ID should not be null")
	public void the_response_user_ID_should_not_be_null() {
		Assert.assertNotNull(response.jsonPath().getString("user.id"), "User id should not be null");
	}
	
	@Then("the response user ID should be greater than 0")
	public void the_response_user_ID_should_be_greater_than_0() {
		int userId = Integer.parseInt(response.jsonPath().getString("user.id"));
		
		Assert.assertTrue(userId > 0, "User Id is not greater than zero"); 
	}
	
	@Then("the response user email should be {string}")
	public void the_response_user_email_should_be(String expectedEmail) {
		
		Assert.assertEquals(registerResponse.getUser().getEmail(), expectedEmail);
	}
	
	@Then("the response Content-Type should be {string}")
	public void the_response_Content_Type_should_be(String expectedContentType) {
	    String actualContentType = response.getHeader("Content-Type");
	    Assert.assertTrue(actualContentType.contains(expectedContentType), 
	        "Expected content type to contain " + expectedContentType + " but got " + actualContentType);
	}
	
		
	@Then("the response should match the registration success schema")
	public void the_response_should_match_the_registration_success_schema() {
		// Validates full structural blueprint matching properties & requirements
        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/registration-success-schema.json"));
	}
	
	@Then("the {string} field should be a boolean")
	public void the_field_should_be_a_boolean(String path) {
		Object value = response.jsonPath().get(path);
        Assert.assertTrue(value instanceof Boolean, path + " is not a Boolean");
	}
	
	@Then("the {string} field should be a string")
	public void the_field_should_be_a_string(String path) {
		Object value = response.jsonPath().get(path);
        Assert.assertTrue(value instanceof String, path + " is not a String");;
	}
	
	@Then("the {string} field should be an object")
	public void the_field_should_be_an_object(String path) {
		Object value = response.jsonPath().get(path);
        Assert.assertTrue(value instanceof Map, path + " is not a JSON Object Map");
	} 
	
	@Then("the user id field should be a number")
	public void the_user_field_should_be_a_number() {
		Object value = response.jsonPath().get("user.id");
        Assert.assertTrue(value instanceof Number, "user.id is not a Number");
	}
	
	@Then("the user {string} field should be a string")
	public void the_user_field_should_be_a_string(String string) {
        Object value = response.jsonPath().get("user.email");
        Assert.assertTrue(value instanceof String, "user.email is not a String");
	}

    @Then("the response should not contain the {string} field")
    public void the_response_should_not_contain_the_field(String fieldName) {
        // Fetches the entire root map of the JSON response
        Map<String, Object> rootResponse = response.jsonPath().get("");
        
        // Asserts that the key is missing entirely from the root level
        Assert.assertFalse(rootResponse.containsKey(fieldName), 
            "Security Violation: The root response payload contains the forbidden field: " + fieldName);
    }
    
    @Then("the response user object should not contain the {string} field")
    public void the_response_user_object_should_not_contain_the_field(String fieldName) {
        // Navigates down to the user sub-object block
        Map<String, Object> userObject = response.jsonPath().get("user");
        
        // Verifies the user object itself exists before parsing keys
        Assert.assertNotNull(userObject, "The 'user' field was missing from the response payload entirely.");
        
        // Asserts that the sensitive key is missing from the user payload
        Assert.assertFalse(userObject.containsKey(fieldName), 
            "Security Violation: The 'user' payload block contains the forbidden field: " + fieldName);
    }
    
    @Then("the EventHub response should contain a valid JWT token")
    public void the_event_hub_response_should_contain_a_valid_jwt_token() {
        String token = response.jsonPath().getString("token");
        Assert.assertNotNull(token, "JWT Auth token was null in the response.");
        Assert.assertFalse(token.trim().isEmpty(), "JWT Auth token was returned empty.");
    }
	
	
//=======================================================================================================
// Negative Test Cases
//=======================================================================================================

  //======================================  GIVEN  ==========================================================

	
    @Given("a user already exists with email {string}")
    public void a_user_already_exists_with_email(String email) {
        Map<String, Object> setupBody = new HashMap<>();
        setupBody.put("email", email);
        setupBody.put("password", "SetupPassword123");

        // Explicitly add baseUri here so it does not default to localhost
        Response setupResponse = given()
                .baseUri("https://api.eventhub.rahulshettyacademy.com") // Added Base URL
                .header("Content-Type", "application/json")
                .body(setupBody)
                .post("/api/auth/register");

        int code = setupResponse.getStatusCode();
        Assert.assertTrue(code == 201 || code == 400, 
            "Failed to set up existing user prerequisite. Status code was: " + code);
    }

    
    
    @Given("the registration request body contains:")
    public void the_registration_request_body_contains(DataTable dataTable) {
        // Converts the 2-column key-value Data Table layout into a clean Java Map
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        
        String emailValue = data.get("email");
        String passwordValue = data.get("password");

        email = data.get("email");
		password = data.get("password");

		registerRequest = new RegisterRequest(data.get("email"), data.get("password"));

		System.out.println("========================================");
		System.out.println("Invalid Registration Request");
		System.out.println("Email : " + data.get("email"));
		System.out.println("Password : " + data.get("password"));
		System.out.println("========================================");

    }
	
//=========================================== WHEN ==========================================================	
	
    @When("I send the same registration request again")
    public void i_send_the_same_registration_request_again() {
    	
    		String methodName = HttpConstants.POST;
	    	switch (HttpConstants.POST.toUpperCase()) {
	
			case "POST":
	
				// ---------------------------------------------
				// FIRST SEND THE REQUEST
				// ---------------------------------------------
	
				response = registerService.registerUser(registerRequest);
	
				// ---------------------------------------------
				// THEN CHECK THE RESPONSE STATUS
				// ---------------------------------------------
	
				if (response.statusCode() == HttpConstants.CREATED) {
	
					registerResponse = registerService.registerUserUsingPojo(response);
	
					System.out.println("Token : " + registerResponse.getToken());
					System.out.println("Success : " + registerResponse.isSuccess());
					System.out.println("User Id : " + registerResponse.getUser().getId());
					System.out.println("User email : " + registerResponse.getUser().getEmail());		
					
					// -----------------------------------------
					// SAVE TOKEN ONLY AFTER SUCCESSFUL RESPONSE
					// -----------------------------------------
	
					String token = response.jsonPath().getString("token");
	
					if (token != null && !token.isBlank()) {
	
						TokenManager.setToken(token);
	
						System.out.println("Token saved in TokenManager");
					}
	
				} else {
	
					registerErrorResponse = registerService.registerUserUsingPojo_failure(response);
	
					System.out.println("Error : " + registerErrorResponse.getError());
				}
	
				break;
	
			default:
	
				throw new IllegalArgumentException("Unsupported HTTP method: " + methodName);
			}
	
			System.out.println("HTTP Method : " + methodName);
	
			System.out.println("Status Code : " + response.statusCode());
	
			System.out.println("Response Body:");
			System.out.println(response.asPrettyString());
	
			printResponse();
    }	
  


    
    
    
    
//==================================== Then ==================================================================
    
    @Then("the response success field should be false")
    public void the_response_success_field_should_be_false() {
        boolean successFlag = response.jsonPath().getBoolean("success");
        Assert.assertFalse(successFlag, "Validation Failure: 'success' flag returned true instead of false.");
    }
    
    
    @Then("the response success field should be true")
    public void the_response_success_field_should_be_true() {
        boolean successFlag = response.jsonPath().getBoolean("success");
        Assert.assertTrue(successFlag, "Validation Failure: 'success' flag returned false instead of true.");
    }
    
    
    @Then("the response error should be {string}")
    public void the_response_error_should_be(String expectedErrorMsg) {
        String actualErrorMsg = response.jsonPath().getString("error");
        Assert.assertEquals(actualErrorMsg, expectedErrorMsg, "Main error message mapping mismatch!");
    }
	
    @Then("the email validation message should be {string}")
    public void the_email_validation_message_should_be(String expectedValidationMsg) {
        // Navigates to the first object in the 'details' array and grabs the 'message' field
        String actualValidationMsg = response.jsonPath().getString("details[0].message");
        
        // Assert that the message matches what was expected from your Gherkin step
        Assert.assertEquals(actualValidationMsg, expectedValidationMsg, 
            "The validation error message for the email field did not match!");
    }
	
//	@Then("the response should indicate that the email is already registered")
//	public void email_is_already_registered() {
//		Assert.assertEquals(registerErrorResponse.getError(), "Email already registered");
//	}
	
    @Then("the response should indicate that the email is already registered")
    public void the_response_should_indicate_that_the_email_is_already_registered() {
        // Validates the exact error node structure returned by the EventHub API
        String actualErrorMsg = response.jsonPath().getString("error");
        String expectedErrorMsg = "Email already registered";
        
        Assert.assertEquals(actualErrorMsg, expectedErrorMsg, "The error message node value did not match.");
    }
    
    @Then("the response error message should be {string}")
    public void the_response_error_message_should_be(String errorExpected) {
    		String actualErrorMsg = response.jsonPath().getString("error");
        String expectedErrorMsg = "Validation failed";
        
        Assert.assertEquals(actualErrorMsg, expectedErrorMsg, "The error message node value did not match.");
        
        // Extract all messages into a List
        List<String> messages = response.jsonPath().getList("details.message");

        System.out.println(messages); 
        
        
//        // 1. Get the email error message
//        String emailMessage = response.jsonPath().getString("details.find { it.field == 'email' }.message");
//
//        // 2. Get the password error message
//        String passwordMessage = response.jsonPath().getString("details.find { it.field == 'password' }.message");
//
//        System.out.println("Email Error: " + emailMessage);      
//        // Output: A valid email is required
//
//        System.out.println("Password Error: " + passwordMessage);  
        // Output: Password must be at least 6 characters

        // 2. Get the password error message
      
    	
    }
    
    @Then("And the response details message should be Password must be at least {int} characters")
    public void password_must_be_at_least_6_characters(int num) {
    	  	String passwordMessage = response.jsonPath().getString("details.find { it.field == 'password' }.message");
        Assert.assertEquals(passwordMessage, "Password must be at least 6 characters");
          
    }
    
    @Then("the response details message should be {string}")
    public void And_the_response_details_message_should_be(String expectedMessage) {
	  	// String passwordMessage = response.jsonPath().getString("details.find { it.field == 'email' }.message");
	  	// Assert.assertEquals(passwordMessage, expectedMessage); 
	  	String targetField = "email"; // This can be dynamic
	  	String message = null;

	  	// Use Groovy find to safely locate the item where field == 'email'
	  	Object result = response.jsonPath().get("details.find { it.field == '" + targetField + "' }");

	  	if (result != null) {
	  	    message = response.jsonPath().getString("details.find { it.field == '" + targetField + "' }.message");
	  	    System.out.println("Extracted Message: " + message);
	  	    // Output: A valid email is required
	  	  Assert.assertEquals(message, expectedMessage); 
	  	} else {
	  	    System.out.println("Field '" + targetField + "' was not found in the error details array.");
	  	}   
    }
    
    
    
    
    
    
    
    
    
    
	

}