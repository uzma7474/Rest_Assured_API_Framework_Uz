package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import io.restassured.response.Response;
import models.requests.RegisterRequest;
import models.response.RegisterErrorResponse;
import models.response.RegisterResponse;
import services.RegisterService;
import utils.TokenManager;

import java.util.Map;

import static io.restassured.RestAssured.*;

import org.testng.Assert;

import constants.HttpConstants;
import endpoints.RegisterEndpoints;

public class RegisterSteps {

	private String email;
	private String password;

	private String registrationToken;
	private int userId;

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
	
	
}