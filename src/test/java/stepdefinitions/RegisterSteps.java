package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import io.restassured.response.Response;
import models.requests.RegisterRequest;
import models.response.RegisterResponse;
import services.RegisterService;

import java.util.Map;

import static io.restassured.RestAssured.*;

import org.testng.Assert;

public class RegisterSteps {

	private String email;
	private String password;
	
	public RegisterRequest registerRequest;
	
	
	public RegisterService registerService = new RegisterService();
	public RegisterResponse res ;

	private Response response;

//	@Given("I have valid registration details")
//	public void i_have_valid_registration_details(DataTable dataTable) {
//
//		Map<String, String> data = dataTable.asMaps().get(0);
//
//		email = data.get("email");
//		password = data.get("password");
//
//		System.out.println("Email    : " + email);
//		System.out.println("Password : " + password);
//	}
	
	
	// ========================================================= 
	// GIVEN 
	// ========================================================= 
	
	@Given("I have valid registration details") 
	public void i_have_valid_registration_details(DataTable dataTable) {
		Map<String, String> data = dataTable.asMaps(String.class, String.class) .get(0); 
		
		registerRequest = new RegisterRequest( data.get("email"), data.get("password") ); 
		
		System.out.println("========================================"); 
		System.out.println("Registration Request"); 
		System.out.println("Email : " + data.get("email")); 
		System.out.println("Password : " + data.get("password")); 
		System.out.println("========================================"); 
	}


	// ========================================================= 
	// GIVEN 
	// ========================================================= 
	
	@Given("I have invalid registration details")
	public void i_have_invalid_registration_details(DataTable dataTable) {

		Map<String, String> data = dataTable.asMaps().get(0);

		email = data.get("email");
		password = data.get("password");

		System.out.println("Email    : " + email);
		System.out.println("Password : " + password);
	}

//	@When("I send a POST request to register the user")
//	public void i_send_a_post_request_to_register_the_user() {
//
//		String requestBody = """
//				{
//				    "email": "%s",
//				    "password": "%s"
//				}
//				""".formatted(email, password);
//
//		response = given().contentType("application/json").accept("application/json").body(requestBody).when()
//				.post("/api/auth/register");
//
//		System.out.println("Response Status: " + response.statusCode());
//		System.out.println("Response Body:");
//		System.out.println(response.asPrettyString());
//	}
	
	// ========================================================= 
	// WHEN 
	// ========================================================= 
	@When("I send a {string} request to register the user") 
	public void i_send_a_request_to_register_the_user( String methodName) {
		
		switch (methodName.toUpperCase()) { 
			case "POST": 
				response = registerService.registerUser( registerRequest ); 
				res = registerService.registerUserIn(response);
				System.out.println("Token in classe : "+res.getToken());
				break; 
			default: 
				throw new IllegalArgumentException( "Unsupported HTTP method: " + methodName ); 
		} 
		System.out.println("HTTP Method : " + methodName); 
		System.out.println("Status Code : " + response.statusCode()); 
		System.out.println("Response Body:"); System.out.println(response.asPrettyString()); 
	}
	

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
	
	@Then("the registration response success should be {word}")
	public void the_registration_response_success_should_be(String expectedSuccess) {
		boolean expected =  Boolean.parseBoolean(expectedSuccess);

	    boolean actual = response.jsonPath().getBoolean("success");
	    
	    String token = response.jsonPath().getString("token");
	    System.out.println("Token : "+token);

		Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success to be true");

		Assert.assertNotNull(response.jsonPath().getString("token"), "JWT token should not be null");

		Assert.assertNotNull(response.jsonPath().getString("user.email"), "User email should not be null");
	}
}