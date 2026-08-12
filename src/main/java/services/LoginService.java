package services;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.requests.RegisterRequest;
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
	public RegisterRequest registerRequest;
	public RequestSpecification requestSpecTextContent;
	
	
	public LoginService() {
		this.requestSpecification = getRequestSpec();
		
		//this.registerRequest = new RegisterRequest(ConfigManager.getProperty("email"), ConfigManager.getProperty("password"));
		this.registerRequest = new RegisterRequest();
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
	
	
	public RegisterResponse loginUserUsingPojo(Response res) {
		Response response = res.then()
								.extract()
								.response();
		
		if (response.statusCode() == 201) {
	        return response.as(RegisterResponse.class);
	    }

	    return null;
						
	}
	
	

}
