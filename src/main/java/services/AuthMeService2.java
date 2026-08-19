package services;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.response.AuthMeResponse;
import models.response.AuthResponse;
import models.response.BaseErrorResponse;
import models.response.LoginResponse;
import models.response.UserAuthResponse;
import utils.TokenManager;

import static io.restassured.RestAssured.given;

import base.BaseApi;
import endpoints.AuthEndpoints;

public class AuthMeService2 extends BaseApi {
	
	public RequestSpecification requestSpecification;

	public AuthMeService2() {
		// this.requestSpecification = getAuthenticatedRequestSpec();
		this.requestSpecification = getRequestSpec();
		
	}


    public Response getCurrentUserAuthMe(String authEndpoint) {
    	
        return given()
        			.spec(requestSpecification)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + TokenManager.getToken())
            .when()
                .get(AuthEndpoints.AuthMe);
    }
    
    
    public BaseErrorResponse basicAuthInsteadBearerAuth() {
    	
        Response response =  given()
				        			.spec(requestSpecification)
				                .header("Accept", "application/json")
				                .header("Authorization", "basic " + TokenManager.getToken())
				             .when()
					            .get(AuthEndpoints.AuthMe)
					         .then()
					         	.extract()
					         	.response();
				        
	    	if (response.statusCode() != 200) {
				return response.as(BaseErrorResponse.class);
			}
			
			return null;

    }
    
    public BaseErrorResponse bearerTokenWithExtraData(String token) {
    	
        Response response =  given()
				        			.spec(requestSpecification)
				                .header("Accept", "application/json")
				                .header("Authorization", token)
				             .when()
					            .get(AuthEndpoints.AuthMe)
					         .then()
					         	.extract()
					         	.response();
				        
	    	if (response.statusCode() != 200) {
				return response.as(BaseErrorResponse.class);
			}
			
			return null;

    }
    
    
    
    
    public BaseErrorResponse userAuthMeWithoutAuthorization(String authEndpoint) {
    	
		    	Response response =  given()
		    			.spec(requestSpecification)
		            .header("Accept", "application/json")
		            .header("Authorization", TokenManager.getToken())
		        .when()
		            .get(AuthEndpoints.AuthMe)
		            .then()
		         	.extract()
		         	.response();
		if (response.statusCode() != 200) {
			return response.as(BaseErrorResponse.class);
		}
		
		return null;

    }
    
    public BaseErrorResponse userAuthMeWithoutBearerAuthorization() {
    	
    		Response response =  given()
			        			.spec(requestSpecification)
			                .header("Accept", "application/json")
			                .header("Authorization", TokenManager.getToken())
			            .when()
			                .get(AuthEndpoints.AuthMe)
			                .then()
			             	.extract()
			             	.response();
        if (response.statusCode() != 200) {
	        return response.as(BaseErrorResponse.class);
	    }

	    return null;
    }
    
    
    
    public BaseErrorResponse userAuthMeEmptyAuthorization() {
    	
        Response response =  given()
			        			.spec(requestSpecification)
			                .header("Accept", "application/json")
			                .header("Authorization", "")
			            .when()
			                .get(AuthEndpoints.AuthMe)
			             .then()
			             	.extract()
			             	.response();
        if (response.statusCode() != 200) {
	        return response.as(BaseErrorResponse.class);
	    }

	    return null;
    }
    
	public AuthResponse authMeUserUsingPojo(Response res) {
		Response response = res.then()
								.extract()
								.response();
		
		if (response.statusCode() == 200) {
	        return response.as(AuthResponse.class);
	    }

	    return null;
						
	}
	
	public BaseErrorResponse userAuthMeWithAuthorization(String authorizationValue) {

	    RequestSpecification request = given()
							            .spec(requestSpecification)
							            .header("Accept", "application/json");

	    // Only add Authorization header when authorizationValue is not null
	    if (authorizationValue != null) {
	        request.header("Authorization", authorizationValue);
	    }

	    Response response = request.when()
				             .get(AuthEndpoints.AuthMe)
				           .then()
				             	.extract()
				             	.response();
	    System.out.println("Response Error : "+response.statusCode());
        if (response.statusCode() != 200) {
	        return response.as(BaseErrorResponse.class);
	    }

	    return null;	           
	}
	
	
	
	public AuthMeResponse authMeUser(Response res) {
		Response response = res.then()
								.extract()
								.response();
		
		if (response.statusCode() == 200) {
	        return response.as(AuthMeResponse.class);
	    }

	    return null;
						
	}
    
    
	public BaseErrorResponse authMeUserUsingPojoFailure(Response res) {
		Response response = res.then()
								.extract()
								.response();
		
		if (response.statusCode() != 200) {
	        return response.as(BaseErrorResponse.class);
	    }

	    return null;
						
	}
    
    
    
}