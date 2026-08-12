package stepdefinitions;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.requests.RegisterRequest;
import models.response.BaseResponse;
import models.response.RegisterErrorResponse;
import models.response.RegisterResponse;
import services.LoginService;
import services.RegisterService;

public class LoginSteps {
	
	private String email;
	private String password;

	private String registrationToken;
	private int userId;

    private Response authenticatedResponse;
    private String jwtToken;
	
    private RequestSpecification request;
    
	public RegisterRequest registerRequest;

	public LoginService loginService = new LoginService();
	public RegisterResponse loginResponse;
	public BaseResponse loginErrorResponse;

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
	
	
	
//======================================  WHEN  ==========================================================
	
	
//======================================  THEN  ==========================================================
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
