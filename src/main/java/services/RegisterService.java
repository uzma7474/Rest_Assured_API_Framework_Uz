package services;

import base.BaseApi;
import config.ConfigManager;
import endpoints.RegisterEndpoints;
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


public class RegisterService extends BaseApi {

	private RequestSpecification requestSpecification;
	public RegisterRequest registerRequest;
	 
	
	
	public RegisterService() {
		this.requestSpecification = getRequestSpec();
		
		//this.registerRequest = new RegisterRequest(ConfigManager.getProperty("email"), ConfigManager.getProperty("password"));
		this.registerRequest = new RegisterRequest();
	}

	/**
	 * Register a new user.
	 *
	 * @param registerRequest registration request payload
	 * @return API response
	 */
	public Response registerUser(RegisterRequest registerRequest) {

		return given()
					.spec(requestSpecification)
					.body(registerRequest)
			  .when()
			     	.post(RegisterEndpoints.REGISTER);
	}
	
	public RegisterResponse registerUserUsingPojo(Response res) {
		Response response = res.then()
								.extract()
								.response();
		
		if (response.statusCode() == 201) {
	        return response.as(RegisterResponse.class);
	    }

	    return null;
						
	}
	
	public RegisterErrorResponse registerUserUsingPojo_failure(Response res) {
		Response response = res.then()
								.extract()
								.response();
		
		if (response.statusCode() != 201) {
	        return response.as(RegisterErrorResponse.class);
	    }

	    return null;
						
	}
	
}