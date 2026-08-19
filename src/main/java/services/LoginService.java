package services;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.requests.RegisterRequest;
import models.response.BaseErrorResponse;
import models.response.LoginErrorResponse;
import models.response.LoginResponse;
import models.response.RegisterErrorResponse;
import models.response.RegisterResponse;

//Core REST Assured methods (given(), when(), get(), post(), etc.)
import static io.restassured.RestAssured.*;

//Matchers for validating responses (equalTo(), hasItem(), containsString(), etc.)
import static org.hamcrest.Matchers.*;

import base.BaseApi;
import endpoints.AuthEndpoints;
import endpoints.RegisterEndpoints;

//REST Assured specific matchers (e.g., matchesXsd() for XML validation)
import static io.restassured.matcher.RestAssuredMatchers.*;

public class LoginService extends BaseApi{
	
	private RequestSpecification requestSpecification;
	public RegisterRequest loginRequest;
	public RequestSpecification requestSpecTextContent;
	
	
	public LoginService() {
		this.requestSpecification = getRequestSpec();
		
		//this.registerRequest = new RegisterRequest(ConfigManager.getProperty("email"), ConfigManager.getProperty("password"));
		this.loginRequest = new RegisterRequest();
	}
	
	/**
	 * Login a new user.
	 *
	 * @param registerRequest Login request payload
	 * @return API response
	 */
	public Response loginUser(RegisterRequest loginRequest) {

		return given()
					.spec(requestSpecification)
					.body(loginRequest)
			  .when()
			     	.post(AuthEndpoints.LOGIN);
	}
	
	
	public LoginResponse loginUserUsingPojo(Response res) {
		Response response = res.then()
								.extract()
								.response();
		
		if (response.statusCode() == 200) {
	        return response.as(LoginResponse.class);
	    }

	    return null;
						
	}
	
	
	public LoginErrorResponse loginUserUsingPojo_failure(Response res) {
		Response response = res.then()
								.extract()
								.response();
		
		if (response.statusCode() != 200) {
	        return response.as(LoginErrorResponse.class);
	    }

	    return null;
						
	}
	
	
	public void authenticateUserUsingBearerToken(String token) {
		
	}
	
	
	
	

}
