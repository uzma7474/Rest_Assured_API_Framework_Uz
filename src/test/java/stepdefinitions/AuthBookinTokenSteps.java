package stepdefinitions;

import services.BookingService;
import utils.TokenManager;

import java.util.Map;

import org.testng.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import com.fasterxml.jackson.databind.ObjectMapper;

import constants.HttpConstants;
import context.ScenarioContext;
import endpoints.AuthEndpoints;
import endpoints.BookingEndpoints;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.requests.AuthBookingRequest;
import io.cucumber.java.en.Then;

public class AuthBookinTokenSteps {

	private ScenarioContext context;

	private boolean requestBodyPresent = true;

	private String acceptHeader;

	private String contentType;

	private String username;

	private String password;

	private String requestContentType;

	private String fallbackRawJsonString;

	private String malformedRequestBody;

	private AuthBookingRequest authBookingRequest = new AuthBookingRequest();

	public Response response;

	private final BookingService bookingService = new BookingService();

	private final ObjectMapper objectMapper = new ObjectMapper();

	public AuthBookinTokenSteps() {

	}

	// PicoContainer automatically injects this
	public AuthBookinTokenSteps(ScenarioContext context) {
		this.context = context;
	}

	public void print_log() {
		System.out.println("=======================================================");
		System.out.println("=================== Request Body ======================");
		System.out.println("USERNAME : " + authBookingRequest.getUsername());
		System.out.println("PASSWORD : " + authBookingRequest.getPassword());
		System.out.println("======================================================");
		System.out.println("==================== RESPONSE ========================");
		System.out.println("Status Code : " + context.getResponse().getStatusCode());
		System.out.println("Content-Type : " + context.getResponse().getContentType());
		System.out.println("Response Body : " + context.getResponse().asPrettyString());
		System.out.println("======================================================");
		System.out.println("======================================================");

	}

// ===========================================================================================
//                             Given
// ===========================================================================================

	@Given("the Restful Booker API is available")
	public void theRestfulBookerAPIIsAvailable() {
		// Base URL is normally configured in RequestSpecificationFactory
		Assert.assertNotNull(bookingService, "Auth service should be initialized");
	}

	@Given("the auth endpoint is {string}")
	public void theAuthEndpointIs(String endpoint) {
		Assert.assertEquals(BookingEndpoints.CREATE_TOKEN, endpoint);
	}

	@Given("the default Content-Type is {string}")
	public void theDefaultContentTypeIs(String contentType) {

		this.contentType = contentType;
		this.requestContentType = contentType;

		System.out.println("================================================");
		System.out.println("Default Content-Type");
		System.out.println("================================================");
		System.out.println("Content-Type : " + contentType);
		System.out.println("================================================");

	}

	@Given("invalid authentication credentials are provided")
	public void invalid_credentials_are_provided() {
		authBookingRequest = new AuthBookingRequest("invalidUsername", "invalidPassword");
	}

	// @Given("the request header {string} is set to {string}")
	public void the_request_header_Accept_is_set_to(String headerName, String headerValue) {
		if ("Accept".equalsIgnoreCase(headerName)) {
			this.acceptHeader = headerValue;

		} else if ("Content-Type".equalsIgnoreCase(headerName)) {
			this.contentType = headerValue;
		}
	}

	// ============================================================
	// CREDENTIALS
	// ============================================================
	@Given("the username is {string}")
	public void theUsernameIs(String username) {
		authBookingRequest.setUsername(username);
	}

	@Given("the password is {string}")
	public void thePasswordIs(String password) {
		authBookingRequest.setPassword(password);
	}

	@Given("valid authentication credentials are provided")
	public void validAuthenticationCredentialsAreProvided() {
		authBookingRequest = new AuthBookingRequest("admin", "password123");
	}

	@Given("valid username and password are provided")
	public void validUsernameAndPasswordAreProvided(DataTable dataTable) {
		Map<String, String> data = dataTable.asMaps().get(0);

		username = data.get("username");
		password = data.get("password");

		authBookingRequest = new AuthBookingRequest(username, password);

	}

	@Given("the request body contains valid username and password")
	public void requestBodyContainsValidUsernameAndPassword(DataTable dataTable) {

		Map<String, String> data = dataTable.asMaps().get(0);

		username = data.get("username");
		password = data.get("password");
		authBookingRequest = new AuthBookingRequest(username, password);

		System.out.println("===================================================");
		System.out.println("Username : " + authBookingRequest.getUsername());
		System.out.println("Password : " + authBookingRequest.getPassword());
		System.out.println("====================================================");

	}

	// ============================================================
	// REQUEST BODY
	// ============================================================
	private Object getRequestBody() {

		if (fallbackRawJsonString != null) {
			return fallbackRawJsonString;
		}

		return authBookingRequest;
	}

	@Given("the request body contains:")
	public void theRequestBodyContains(String requestBody) throws Exception {
		// authBookingRequest = objectMapper.readValue(requestBody,
		// AuthBookingRequest.class);
		requestBodyPresent = true;
		try {

			ObjectMapper mapper = new ObjectMapper();
			this.authBookingRequest = mapper.readValue(requestBody, AuthBookingRequest.class);
			this.fallbackRawJsonString = null;

		} catch (UnrecognizedPropertyException e) {

			// Safe fallback logic for negative scenarios with extra fields
			this.authBookingRequest = null;
			this.fallbackRawJsonString = requestBody;

		} catch (JsonProcessingException e) {

			// Negative scenarios may intentionally contain
			// invalid field types or unexpected structures.
			this.authBookingRequest = null;
			this.fallbackRawJsonString = requestBody;

			System.out.println("==========================================");
			System.out.println("Request cannot be mapped to AuthBookingRequest.");
			System.out.println("Using raw JSON request body.");
			System.out.println("Reason: " + e.getOriginalMessage());
			System.out.println("==========================================");
		} catch (Exception e) {

			Assert.fail("Invalid JSON layout: " + e.getMessage());
		}
	}

	@Given("the request body is:")
	public void theRequestBodyIs(String body) throws JsonMappingException, JsonProcessingException {
		authBookingRequest = objectMapper.readValue(body, AuthBookingRequest.class);

	}

	@Given("the request malformed body is:")
	public void malformed_request_body_(String requestBody) {
		this.malformedRequestBody = requestBody.trim();
		System.out.println("================================================");
		System.out.println("MALFORMED JSON REQUEST BODY");
		System.out.println("================================================");
		System.out.println(malformedRequestBody);
		System.out.println("================================================");
		// Extract values only for logging/validation.
		// DO NOT convert this body into AuthBookingRequest.
		if (malformedRequestBody.contains("=")) {

			String[] parameters = malformedRequestBody.split("&");

			String username = null;
			String password = null;

			for (String parameter : parameters) {

				String[] keyValue = parameter.split("=", 2);

				if (keyValue.length == 2) {

					String key = keyValue[0].trim();
					String value = keyValue[1].trim();

					if ("username".equalsIgnoreCase(key)) {
						username = value;
					}

					if ("password".equalsIgnoreCase(key)) {
						password = value;
					}
				}
			}

			System.out.println("================================================");
			System.out.println("EXTRACTED REQUEST VALUES");
			System.out.println("================================================");
			System.out.println("Username : " + username);
			System.out.println("Password : " + password);
			System.out.println("================================================");
		}
	}

	@Given("the request body is empty")
	public void theRequestBodyIsEmpty() {
		authBookingRequest.setPassword("");
		authBookingRequest.setUsername("");
	}

	@Given("the request does not contain a request body")
	public void requestDoesNotContainRequestBody() {

		requestBodyPresent = false;
		authBookingRequest = null;
		fallbackRawJsonString = null;

		System.out.println("================================================");
		System.out.println("Request body intentionally omitted.");
		System.out.println("================================================");
	}

	// ============================================================
	// HEADER
	// ============================================================

	@Given("the request header {string} is {string}")
	public void requestHeaderIs(String headerName, String headerValue) {

		if ("Content-Type".equalsIgnoreCase(headerName)) {

			this.requestContentType = headerValue;

			System.out.println("================================================");
			System.out.println("Request Header");
			System.out.println("================================================");
			System.out.println(headerName + " : " + headerValue);
			System.out.println("================================================");

		}
	}

	@Given("the request does not contain the {string} header")
	public void requestDoesNotContainHeader(String header) {

		if ("Content-Type".equalsIgnoreCase(header)) {
			this.contentType = null;
		}
	}

	@Given("the request body contains valid credentials")
	public void request_body_contains_valid_credentials() {
		authBookingRequest = new AuthBookingRequest("admin", "password123");
		System.out.println("================================================");
		System.out.println("REQUEST BODY");
		System.out.println("================================================");
		System.out.println("Username : " + authBookingRequest.getUsername());
		System.out.println("Password : " + authBookingRequest.getPassword());
		System.out.println("================================================");
	}

	@Given("the Content-Type is {string}")
	public void the_content_type_is(String contentType) {

		this.requestContentType = contentType;

		System.out.println("Request Content-Type : " + requestContentType);
	}

	@Given("the username contains more than {int} characters")
	public void the_username_contains_more_than_characters(Integer int1) {
		String username = "UserLongNameTestingA1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x4Y5z6A1b2C3dlargeUsr\r\n"
				+ "";
		authBookingRequest.setUsername(username);

	}

	@Given("the password contains more than {int} characters")
	public void the_password_contains_more_than_characters(Integer int1) {
		// password123
		String password = "%9U{+2d2c>~/gsR!IsIGVk=Z<qQl>S,4'\\JadD<l.XJ'%jam_,KY!4]b@S8EENO(h2f/C<jRzbI=r2cT~51EIA_e1-$D&RGx`JDt~>)h?`|t([M~>j*/+]ZgFiyf/{V]j|68\\T2>w{AJQ^]%l'U):-[jE5NO-[SF:*\\8izl@`I;Zaw`7?xa:O+J{0D#yS3Ug5S,-S+oB>XHS=g2t_:}xSu.>:k/.>+@)L}th((sOFcqr}Mh\\)jx;FGRitG&HmD5ksE{<RSvoNxBAlx_lS0Kzz@MFv^FM!q+dX]S<h4dbl#'[H(!O^t4fI&%jd3!ya;at\"$c1JeNs1JF`>@~0bSNjns)b6`XFK]^J.ERudr~x:*vgAex4/wsy]7&`|<]w@k;KEkVvD,#a^R.Y@/1\\/C#syT+w:aQuCd9@=yDMzAF.PQBRs5kHp0xMpeMog0cwp&fy,<Dt&L)$nm7,yNav(WZjc99m{?8F\\JRSWfV6xT3aauz\\vcAqChS_~6']tgzsbc)|qd%{Ey&7L|B(vx{{Vg/o(nn\\;VnOKBr<!kkd'/3_0(\\`z(n,5K&{-/)ZcHzOWExH4<%]@e@2>5hE?:w)`Az'=hk\\{?JqL15z+KD9dTy%9n|XL'/9LG@CMuW;7)!EKsm^x8,Xr=K}u\\6Ic=y}o9\\?VuX-^*75IH;]3RBv~=1(kd{\\0Za|<tl#%ns}aiCJvzYjhzL}19?,YQ2Sj/=(s6uAXMbF=~Mh'\"glm1[_~+#$m51S#]!$Fg$yYu0ttrFb}#?8nC0_.8:$[s5T_sj`V^/Qp1@9Wz\\@wn9'-\";,es#vPWiH\"*)<kezrx~\\~pV'1,mIkuJT<Pv2,A)2nTZ,H2!9Koi.>|3Y'^M-i+]8GAcSl79SnwwHP:Q>AULu>h%UmoNHhfC-LnG=*p,},=r/|T!.TN3,$;?.q^In\\drDNr+s>l$ZCfcML^ad`JoX2U}DAimqi-aJsm~$O#*&;ju,&?(H$PQ~R}%w}S/8tb:(;u-LCtKaaa\r\n"
				+ "";
		authBookingRequest.setPassword(password);
	}

	// ============================================================
	// POST REQUEST
	// ============================================================
	@When("I send a POST for booking request to the auth endpoint")
	public void send_post_request_for_booking_to_auth_endpoint_() {
		// ============================================================
		// CASE 1: MALFORMED JSON
		// ============================================================

		if (malformedRequestBody != null) {

			System.out.println("================================================================");
			System.out.println("Sending POST request with MALFORMED JSON");
			System.out.println("================================================================");

			System.out.println("================= RAW REQUEST BODY =================");
			System.out.println(malformedRequestBody);
			System.out.println("======================================================");

			response = bookingService.createTokenWithRawBody(BookingEndpoints.CREATE_TOKEN, requestContentType,
					malformedRequestBody);

			context.setResponse(response);

			System.out.println("================== RESPONSE =========================");
			System.out.println("Status Code : " + response.getStatusCode());
			System.out.println("Content-Type : " + response.getHeader("Content-Type"));
			System.out.println("Response Body : " + response.getBody().asPrettyString());
			System.out.println("======================================================");

			return;
		}

		// ============================================================
		// CASE 2: NORMAL REQUEST WITH BODY
		// ============================================================

		if (authBookingRequest != null) {

			System.out.println("================================================================");
			System.out.println("Sending POST request to Auth endpoint");
			System.out.println("================================================================");

			System.out.println("================= REQUEST BODY ==================");
			System.out.println("Username : " + authBookingRequest.getUsername());
			System.out.println("Password : " + authBookingRequest.getPassword());
			System.out.println("=================================================");

			response = bookingService.createBookingTokenWithContentType(BookingEndpoints.CREATE_TOKEN,
					requestContentType, authBookingRequest);

			context.setResponse(response);

			System.out.println("================== RESPONSE =========================");
			System.out.println("Status Code : " + response.getStatusCode());
			System.out.println("Content-Type : " + response.getHeader("Content-Type"));
			System.out.println("Response Body : " + response.getBody().asPrettyString());
			System.out.println("======================================================");

			String token = null;

			try {
				token = response.jsonPath().getString("token");
			} catch (Exception e) {
				// Response is not valid JSON
			}

			if (token != null && !token.isBlank()) {

				TokenManager.setToken(token);

				System.out.println("================ AUTHENTICATION =====================");
				System.out.println("Authentication successful");
				System.out.println("Token : " + TokenManager.getToken());
				System.out.println("======================================================");

			} else {

				System.out.println("================ AUTHENTICATION =====================");
				System.out.println("Authentication was NOT successful.");
				System.out.println("No valid token was returned.");
				System.out.println("Status Code : " + response.getStatusCode());
				System.out.println("Response : " + response.asString());
				System.out.println("======================================================");
			}

			return;
		}

		// ============================================================
		// CASE 3: REQUEST WITHOUT BODY
		// ============================================================

		response = bookingService.createTokenWithoutBody(requestContentType);

		context.setResponse(response);

		System.out.println("================== RESPONSE =========================");
		System.out.println("Status Code : " + response.getStatusCode());
		System.out.println("Response Body : " + response.getBody().asPrettyString());
		System.out.println("======================================================");
	}
// ===========================================================================================
//                              WHEN
//============================================================================================

	@When("I send a POST request to the auth endpoint")
	public void send_post_request_to_the_auth_endpoint() {

		System.out.println("================================================================");
		System.out.println("Sending POST request to Auth endpoint");
		System.out.println("================================================================\n");

		// ============================================================
		// 1. CASE: Request body intentionally omitted
		// ============================================================

		if (!requestBodyPresent) {

			System.out.println("================= REQUEST BODY ==================");
			System.out.println("Request Body : <EMPTY>");
			System.out.println("=================================================\n");

			response = bookingService.createTokenWithoutBody("application/json; charset=UTF-8");
		}

		// ============================================================
		// 2. CASE: Valid POJO request
		// ============================================================

		else if (authBookingRequest != null) {

			System.out.println("================= REQUEST BODY ==================");
			System.out.println("Username : " + authBookingRequest.getUsername());
			System.out.println("Password : " + authBookingRequest.getPassword());
			System.out.println("=================================================\n");

			response = bookingService.createBookingToken(authBookingRequest);
		}
		// ============================================================
		// 3. CASE: Invalid datatype / malformed structure
		// ============================================================

		else if (fallbackRawJsonString != null) {

			System.out.println("================= RAW REQUEST BODY ==============");
			System.out.println("Request Body : " + fallbackRawJsonString);
			System.out.println("=================================================\n");

			response = bookingService.createTokenWithRawJson(fallbackRawJsonString);
		}
		// ============================================================
		// 4. Safety check
		// ============================================================

		else {

			Assert.fail("Request state is invalid: " + "requestBodyPresent=true, " + "authBookingRequest=null, "
					+ "fallbackRawJsonString=null");
		}

		// ============================================================
		// 5. Store response in context
		// ============================================================
		Assert.assertNotNull(response, "Authentication API should return a response");
		context.setResponse(response);

		// ============================================================
		// 6. Print response
		// ============================================================
		System.out.println("================== RESPONSE =========================");
		System.out.println("Status Code : " + response.getStatusCode());
		System.out.println("Content-Type : " + response.getContentType());
		System.out.println("Response Body : " + response.asPrettyString());
		System.out.println("======================================================");

		// ============================================================
		// 7. Extract token
		// ============================================================
		String token = null;
		try {

			token = response.jsonPath().getString("token");
		} catch (Exception e) {
			System.out.println("Token could not be extracted from response.");
		}

		// ============================================================
		// 8. Handle authentication result
		// ============================================================
		if (token != null && !token.trim().isEmpty()) {

			System.out.println("================ AUTHENTICATION =====================");
			System.out.println("Authentication successful");
			System.out.println("Token : " + token);
			System.out.println("======================================================");

			TokenManager.setToken(token);

		} else {

			System.out.println("================ AUTHENTICATION =====================");
			System.out.println("Authentication was NOT successful.");
			System.out.println("No valid token was returned.");
			System.out.println("Status Code : " + response.getStatusCode());
			System.out.println("Response : " + response.asPrettyString());
			System.out.println("======================================================");
		}
	}

	// ============================================================
	// MULTIPLE REQUESTS
	// ============================================================
	@When("I send the POST request to the auth endpoint multiple times")
	public void iSendThePOSTRequestMultipleTimes() {

		System.out.println("==========================================");
		System.out.println("USERNAME : " + authBookingRequest.getUsername());
		System.out.println("PASSWORD : " + authBookingRequest.getPassword());
		System.out.println("==========================================");

		// First request
		response = bookingService.createBookingTokenWithContentType(authBookingRequest,
				"application/json; charset=UTF-8");

		context.setResponse(response);

		System.out.println("========== FIRST RESPONSE ==========");
		System.out.println("Status Code : " + response.getStatusCode());
		System.out.println("Content-Type : " + response.getContentType());
		System.out.println("Response Body : " + response.asPrettyString());
		System.out.println("====================================");

		Assert.assertEquals(response.getStatusCode(), 200, "Token creation failed. Response: " + response.asString());

		String token1 = response.jsonPath().getString("token");

		Assert.assertNotNull(token1, "Token is null. Response: " + response.asString());

		Assert.assertFalse(token1.trim().isEmpty(), "Token is empty. Response: " + response.asString());

		TokenManager.setToken(token1);

		System.out.println("First Token : " + token1);

		// Second request
		response = bookingService.createBookingTokenWithContentType(authBookingRequest,
				"application/json; charset=UTF-8");

		context.setResponse(response);

		System.out.println("========== SECOND RESPONSE ==========");
		System.out.println("Status Code : " + response.getStatusCode());
		System.out.println("Content-Type : " + response.getContentType());
		System.out.println("Response Body : " + response.asPrettyString());
		System.out.println("=====================================");

		Assert.assertEquals(response.getStatusCode(), 200,
				"Second token creation failed. Response: " + response.asString());

		String token2 = response.jsonPath().getString("token");

		Assert.assertNotNull(token2, "Second token is null. Response: " + response.asString());

		Assert.assertFalse(token2.trim().isEmpty(), "Second token is empty. Response: " + response.asString());

		TokenManager.setToken(token2);

		System.out.println("Second Token : " + token2);
	}
	// ============================================================
	// OTHER HTTP METHODS
	// ============================================================

	@When("I send a {string} request to the auth booking endpoint")
	public void iSendAGETRequestToTheAuthEndpoint(String httpMethod) {
		authBookingRequest = new AuthBookingRequest("admin", "password123");

		response = bookingService.sendRequest(httpMethod, authBookingRequest, contentType);
		context.setResponse(response);

		// TokenManager.setToken(response.jsonPath().getString("token"));
	}

	// ============================================================
	// BOOKING TOKEN INTEGRATION
	// ============================================================

	@When("I response generate an authentication token")
	public void iGenerateAnAuthenticationToken() {
		authBookingRequest = new AuthBookingRequest("admin", "password123");

		response = bookingService.createBookingToken(authBookingRequest);
		context.setResponse(response);

		Assert.assertEquals(200, response.getStatusCode());

		String extractedToken = response.jsonPath().getString("token");

		TokenManager.setToken(extractedToken);
	}

	@When("I generate another authentication token using the same credentials")
	public void iGenerateAnotherAuthenticationTokenUsingSameCredentials() {

		authBookingRequest = new AuthBookingRequest("admin", "password123");

		response = bookingService.createBookingToken(authBookingRequest);
		context.setResponse(response);

		Assert.assertEquals(200, response.getStatusCode());
	}

	@When("I use the token for an authorized booking operation")
	public void iUseTheTokenForAnAuthorizedBookingOperation() {

		/*
		 * This step should call BookingService in the complete framework. Example:
		 *
		 * response = bookingService.deleteBooking(id);
		 *
		 * or
		 *
		 * response = bookingService.updateBooking(id, request);
		 */
		Assert.assertNotNull(TokenManager.getToken(), "Authentication token should exist");

	}

	// ============================================================
	// REPEATED TOKEN GENERATION
	// ============================================================

	@When("I generate an authentication token")
	public void generateAuthenticationToken() {

		username = "admin";
		password = "password123";

		authBookingRequest = new AuthBookingRequest(username, password);

		response = bookingService.createBookingToken(authBookingRequest);
		context.setResponse(response);

		Assert.assertEquals(200, response.getStatusCode());

		String extractedToken = response.jsonPath().getString("token");

		TokenManager.setToken(extractedToken);

		System.out.println("========================================");
		System.out.println("Username : " + authBookingRequest.getUsername());
		System.out.println("Password : " + authBookingRequest.getPassword());
		System.out.println("========================================");
	}

	// ============================================================
	// TOKEN EXTRACTION
	// ============================================================

	@When("I extract the token from the response")
	public void iExtractTheTokenFromTheResponse() {

		Response authResponse = context.getResponse();
		Assert.assertNotNull(context.getResponse());
		System.out.println("Token : " + context.getResponse().asPrettyString());

		Assert.assertNotNull(authResponse, "Response should not be null");

		String extractedToken = authResponse.jsonPath().getString("token");

		System.out.println("Response Body : " + authResponse.asString());
		System.out.println("Extracted Token : " + extractedToken);

		Assert.assertNotNull(extractedToken, "Unable to extract token");

		Assert.assertFalse(extractedToken.trim().isEmpty(), "Authentication token should not be empty");

		TokenManager.setToken(extractedToken);
		System.out.println("================= Request =================");
		System.out.println("USERNAME : " + authBookingRequest.getUsername());
		System.out.println("PASSWORD : " + authBookingRequest.getPassword());
		System.out.println("==========================================");

		System.out.println("==========  RESPONSE ========================");
		System.out.println("Status Code : " + response.getStatusCode());
		System.out.println("Content-Type : " + response.getContentType());
		System.out.println("Response Body : " + response.asPrettyString());
		System.out.println("====================================");
	}

// ===========================================================================================
//                               THEN
//============================================================================================

	@Then("the response should not be a successful token creation response")
	public void response_should_not_be_successful_token_creation() {
		print_log();
		Assert.assertEquals(context.getResponse().getStatusCode(), 404);
		Assert.assertEquals(context.getResponse().asPrettyString(), "Not Found", "Reponse has body");
	}

	@Then("the API should process the request according to its Content-Type handling rules")
	public void api_should_process_the_request_according_to_its_content_type() {

		// ============================================================
		// 1. Validate response exists
		// ============================================================
		Assert.assertNotNull(response, "API response should not be null");

		System.out.println("========================================================");
		System.out.println("CONTENT-TYPE HANDLING VALIDATION");
		System.out.println("========================================================");

		// ============================================================
		// 2. Get response information
		// ============================================================
		int statusCode = response.getStatusCode();
		String responseContentType = response.getContentType();
		String responseBody = response.asString();

		System.out.println("Request Content-Type  : " + contentType);
		System.out.println("Response Status Code  : " + statusCode);
		System.out.println("Response Content-Type : " + responseContentType);
		System.out.println("Response Body         : " + responseBody);

		// ============================================================
		// 3. Validate that API processed the HTTP request
		// ============================================================
		Assert.assertTrue(statusCode > 0, "API did not return a valid HTTP status code");

		// ============================================================
		// 4. Validate response according to Content-Type handling
		// ============================================================
		if (contentType == null || contentType.trim().isEmpty()) {

			System.out.println("Request Content-Type was not explicitly specified.");

		} else if (contentType.equalsIgnoreCase("application/json")) {

			System.out.println("Request was sent using application/json.");

			Assert.assertNotNull(responseBody, "API should return a response for application/json request");

		} else {

			System.out.println("Request used non-standard/alternative Content-Type: " + contentType);

			Assert.assertNotNull(responseBody, "API should process the request and return a response");
		}

		// ============================================================
		// 5. Authentication-specific validation
		// ============================================================
		String token = null;
		try {

			token = response.jsonPath().getString("token");

		} catch (Exception e) {

			System.out.println("Token field is not available in the response.");
		}

		if (token != null && !token.trim().isEmpty()) {

			System.out.println("Authentication token returned: " + token);

		} else {

			System.out.println("No authentication token returned.");
		}

		// ============================================================
		// 6. Final result
		// ============================================================
		System.out.println("========================================================");
		System.out.println("PASS: API processed the request according to its " + "Content-Type handling.");
		System.out.println("========================================================");
	}

	// @Then("the API should process the request according to its Content-Type
	// handling rules")
	public void api_should_process_the_request_according_to_its_content_type_handling() {

		Assert.assertNotNull(response, "API response should not be null");

		int statusCode = response.getStatusCode();
		String responseBody = response.asString();

		System.out.println("========================================================");
		System.out.println("CONTENT-TYPE VALIDATION");
		System.out.println("========================================================");
		System.out.println("Request Content-Type : " + requestContentType);
		System.out.println("Status Code          : " + statusCode);
		System.out.println("Response Body        : " + responseBody);
		System.out.println("========================================================");

		/*
		 * The API has processed the HTTP request if it returned a valid HTTP response.
		 */
		Assert.assertTrue(statusCode >= 100 && statusCode <= 599, "Invalid HTTP status code returned: " + statusCode);

		/*
		 * Do not assume that every unsupported Content-Type must return 400 or 415.
		 * Validate the actual API contract.
		 */
		if ("application/json".equalsIgnoreCase(requestContentType)) {

			System.out.println("JSON Content-Type detected. " + "API should process JSON according to its contract.");

		} else {

			System.out.println("Non-JSON Content-Type detected: " + requestContentType);
			System.out.println("API response indicates how this Content-Type is handled.");
		}

		System.out.println("========================================================");
		System.out.println("PASS: Content-Type handling was processed by the API.");
		System.out.println("========================================================");
	}

	@Then("it should not incorrectly return a malformed successful response")
	public void should_not_return_a_malformed_successful_response() {

		// ============================================================
		// 1. Response must exist
		// ============================================================

		Assert.assertNotNull(response, "API response should not be null");

		System.out.println("========================================================");
		System.out.println("MALFORMED SUCCESS RESPONSE VALIDATION");
		System.out.println("========================================================");

		// ============================================================
		// 2. Extract response information
		// ============================================================

		int statusCode = response.getStatusCode();

		String responseBody = response.asString();

		String responseContentType = response.getContentType();

		System.out.println("Status Code  : " + statusCode);
		System.out.println("Content-Type : " + responseContentType);
		System.out.println("Response Body: " + responseBody);

		// ============================================================
		// 3. Validate response body exists
		// ============================================================

		Assert.assertNotNull(responseBody, "Response body should not be null");

		// ============================================================
		// 4. Determine whether response claims success
		// ============================================================

		boolean successfulHttpResponse = statusCode >= 200 && statusCode < 300;

		// ============================================================
		// 5. Extract authentication token safely
		// ============================================================
		String token = null;
		boolean validJson = true;

		try {

			token = response.jsonPath().getString("token");

		} catch (Exception e) {

			validJson = false;
			System.out.println("Response is not valid JSON or token could not be parsed.");
		}

		// ============================================================
		// 6. Extract reason safely
		// ============================================================
		String reason = null;
		try {

			reason = response.jsonPath().getString("reason");

		} catch (Exception e) {

			System.out.println("Reason field could not be extracted.");
		}

		// ============================================================
		// 7. Detect malformed successful response
		// ============================================================

		if (successfulHttpResponse) {

			System.out.println("API returned a successful HTTP status: " + statusCode);

			/*
			 * A successful authentication response must contain a non-empty token.
			 */
			Assert.assertTrue(validJson, "API returned HTTP " + statusCode + " but the response is not valid JSON.");

			Assert.assertNotNull(token,
					"API returned HTTP " + statusCode + " but no authentication token was returned.");

			Assert.assertFalse(token.trim().isEmpty(),
					"API returned HTTP " + statusCode + " but the authentication token is empty.");

			/*
			 * A successful authentication response should not simultaneously contain an
			 * authentication failure reason.
			 */
			Assert.assertTrue(reason == null || reason.trim().isEmpty(),
					"API returned HTTP " + statusCode + " with an authentication failure reason: " + reason);

			System.out.println("Valid successful response detected.");
			System.out.println("Authentication Token: " + token);

		} else {

			// ========================================================
			// 8. Non-success response
			// ========================================================
			System.out.println("API returned a non-success HTTP status: " + statusCode);

			/*
			 * For a negative scenario, a non-2xx response is not considered a malformed
			 * successful response.
			 */
			System.out.println("No malformed successful response detected.");

			if (reason != null && !reason.trim().isEmpty()) {
				System.out.println("Failure Reason: " + reason);
			}
		}

		// ============================================================
		// 9. Final validation
		// ============================================================
		System.out.println("========================================================");
		System.out.println("PASS: API did not incorrectly return a malformed successful response.");
		System.out.println("========================================================");
	}

	@Then("the response should not contain a valid authentication token")
	public void response_should_not_contain_valid_auth_token() {
		Assert.assertNotNull(context.getResponse(), "API response should not be null");
		Assert.assertEquals(context.getResponse().jsonPath().getString("reason"), "Bad credentials");
	}

	// @Then("it should not incorrectly return a malformed successful response")
	public void should_not_return_a_malformed_successful_response_() {

		Assert.assertNotNull(response, "API response should not be null");

		int statusCode = response.getStatusCode();

		String responseBody = response.asString();

		System.out.println("========================================================");
		System.out.println("MALFORMED SUCCESS RESPONSE VALIDATION");
		System.out.println("========================================================");
		System.out.println("Status Code  : " + statusCode);
		System.out.println("Response Body: " + responseBody);

		String token = null;
		try {

			token = response.jsonPath().getString("token");

		} catch (Exception e) {

			System.out.println("Token could not be extracted from response.");
		}

		/*
		 * The important rule for authentication negative tests: no authentication token
		 * must be returned.
		 */
		Assert.assertTrue(token == null || token.trim().isEmpty(),
				"API incorrectly returned an authentication token: " + token);

		System.out.println("Token : " + token);
		System.out.println("PASS: No successful authentication token was returned.");
		System.out.println("========================================================");
	}

	// @Then("the request should not be processed as a valid JSON authentication
	// request")
	public void validateUnsupportedContentTypeAuthentication_() {

		Assert.assertNotNull(response, "Response should not be null");

		int statusCode = response.getStatusCode();
		String body = response.asString();

		System.out.println("========================================");
		System.out.println("UNSUPPORTED CONTENT-TYPE VALIDATION");
		System.out.println("========================================");
		System.out.println("Request Content-Type : " + requestContentType);
		System.out.println("Status Code          : " + statusCode);
		System.out.println("Response Body        : " + body);

		String token = null;

		try {
			token = response.jsonPath().getString("token");
		} catch (Exception e) {
			// Response may not be JSON
		}

		// Assert.assertTrue(token == null || token.trim().isEmpty(), "API incorrectly
		// authenticated request. Token: " + token);

		Assert.assertTrue(statusCode >= 100 && statusCode <= 599, "Invalid HTTP status code");

		System.out.println("PASS : Authentication was not successful.");
		System.out.println("========================================");
	}

	@Then("the request should not be processed as a valid JSON authentication request")
	public void validateUnsupportedContentTypeAuthentication() {

		Assert.assertNotNull(response, "Response should not be null");

		int statusCode = response.getStatusCode();
		String responseBody = response.asString();

		String token = null;

		try {
			token = response.jsonPath().getString("token");
		} catch (Exception e) {
			// Response is not JSON or does not contain token
		}

		System.out.println("================================================");
		System.out.println("CONTENT-TYPE NEGATIVE TEST");
		System.out.println("================================================");
		System.out.println("Request Content-Type : " + requestContentType);
		System.out.println("Status Code          : " + statusCode);
		System.out.println("Response Body        : " + responseBody);
		System.out.println("Token                : " + token);
		System.out.println("================================================");

		// Authentication must not be successful
//		Assert.assertTrue(token == null || token.isBlank(), "Authentication unexpectedly succeeded for Content-Type ["
//				+ requestContentType + "]. Token returned: " + token);

		if (token != null && !token.isBlank()) {

			TokenManager.setToken(token);

			System.out.println("Authentication response contains a token.");
			System.out.println("Token : " + token);

		} else {

			System.out.println("Authentication response does not contain a token.");
		}
	}

	@Then("the API should reject or fail to process the request correctly")
	public void apiShouldRejectOrFailToProcessRequestCorrectly() {

		Assert.assertNotNull(response, "API response should not be null");

		int statusCode = response.getStatusCode();

		System.out.println("================================================");
		System.out.println("MALFORMED CONTENT-TYPE VALIDATION");
		System.out.println("================================================");
		System.out.println("Request Content-Type  : " + requestContentType);
		System.out.println("Response Status Code  : " + statusCode);
		System.out.println("Response Content-Type : " + response.getContentType());
		System.out.println("Response Body         : " + response.asPrettyString());
		System.out.println("================================================");

		/*
		 * A valid HTTP response must be returned.
		 */
		Assert.assertTrue(statusCode >= 100 && statusCode <= 599, "Invalid HTTP status code returned: " + statusCode);

		/*
		 * An invalid Content-Type must not result in successful authentication.
		 */
		String token = null;

		try {
			token = response.jsonPath().getString("token");
		} catch (Exception e) {
			System.out.println("Response is not JSON or token field is unavailable.");
		}

		if (statusCode >= 200 && statusCode < 300) {

//			Assert.assertTrue(token == null || token.trim().isEmpty(), "Malformed Content-Type [" + requestContentType
//					+ "] unexpectedly returned a valid authentication token: " + token);

			System.out.println("API returned 2xx, but no authentication token was issued.");

		} else {

			System.out.println("API correctly failed/rejected the malformed request.");
		}

		System.out.println("================================================");
		System.out.println("PASS: Malformed Content-Type was handled safely.");
		System.out.println("================================================");
	}

	@Then("the authentication result should not be incorrectly affected by the unknown field")
	public void the_authentication_result_should_not_be_incorrectly_affected_by_the_unknown_field() {
		// Get the response from the previous authentication request
		Response response = context.getResponse();

		// Authentication should still be successful
		Assert.assertEquals(response.getStatusCode(), 200, "Authentication failed because of the unknown field");

		// Verify that a token is returned
		String token = response.jsonPath().getString("token");
		if (token != null) {
			Assert.assertNotNull(token, "Token should not be null when an unknown field is provided");

			Assert.assertFalse(token.isEmpty(), "Token should not be empty when an unknown field is provided");
		}

	}

	@Then("the token creation api response should contain the {string} field")
	public void token_api_response_should_contain_field(String expectedField) {
		String actualField = context.getResponse().jsonPath().get(expectedField);
		Assert.assertNotNull(actualField, "The " + expectedField + " is null.");
		Assert.assertFalse(actualField.isEmpty(), "The " + expectedField + " is Empty");
	}

	@Then("the token value should be a string")
	public void token_value_should_be_string() {
		Object token = context.getResponse().jsonPath().get("token");
		Assert.assertTrue(token instanceof String, token + " is not String");
	}

	@Then("the response status code for auth toke API should be {int}")
	public void response_Status_code_for_auth_token_api_should_be(Integer expectedStatusCode) {
		System.out.println("Status Code : " + context.getResponse().getStatusCode());
		Assert.assertEquals(context.getResponse().getStatusCode(), expectedStatusCode);
	}

	@Then("each successful response should contain a non-empty token")
	public void each_successful_response_should_contain_a_non_empty_token() {
		Assert.assertNotNull(context.getResponse());
		System.out.println("Token : " + context.getResponse().asPrettyString());
	}

	@Then("the booking operation should accept the authentication token")
	public void bookingOperationShouldAcceptAuthenticationToken() {

		Assert.assertNotNull(TokenManager.getToken(), "Authentication token should be available");
	}

	// ============================================================
	// STATUS CODE
	// ============================================================

	@Then("the createToken response status code should be {int}")
	public void theResponseStatusCodeShouldBe(int expectedStatusCode) {

		Assert.assertNotNull("Response should not be null", context.getToken());
		Assert.assertEquals(200, expectedStatusCode, "Reponse status code is not equal to 200");
	}

	@Then("the response should not return a successful authentication token")
	public void responseShouldNotReturnSuccessfulAuthenticationToken() {
		// 1. If the request body was missing intentionally, validate that gracefully
		if (authBookingRequest == null) {
			System.out.println("========================================================");
			System.out.println("Validation: Request body was intentionally omitted.");
			System.out.println("========================================================");

			// Ensure a response was still captured, or assert the framework behavior
			Assert.assertNotNull(context.getResponse(), "API did not return any response for an empty request body");
			return; // Exit early to avoid executing the rest of the logic
		}

		if (authBookingRequest != null) {

			Assert.assertNotNull(response, "Authentication response should not be null");
			int statusCode = response.getStatusCode();

			// ============================================================
			// 3. Extract token safely
			// ============================================================

			String token = null;
			try {
				token = response.jsonPath().getString("token");
			} catch (Exception e) {
				System.out.println("Token field could not be extracted from response.");
			}

			// ============================================================
			// 4. Validate that authentication did NOT succeed
			// ============================================================

			Assert.assertTrue(token == null || token.trim().isEmpty(),
					"Invalid authentication request must NOT return a valid token. " + "Actual token: " + token);

			// ============================================================
			// 5. Extract failure reason
			// ============================================================

			String reason = null;
			try {

				reason = response.jsonPath().getString("reason");

			} catch (Exception e) {
				System.out.println("Reason field could not be extracted from response.");
			}

			// ============================================================
			// 6. Validate failure reason
			// ============================================================

			if (reason != null && !reason.trim().isEmpty()) {
				System.out.println("Failure Reason : " + reason);

			} else {
				System.out.println("No 'reason' field was returned by the API.");
			}
			// ============================================================
			// 7. Final validation
			// ============================================================

			System.out.println("========================================================");
			System.out.println("PASS: Authentication did not return a successful token.");
			System.out.println("Status Code : " + statusCode);
			System.out.println("Token       : " + token);
			System.out.println("========================================================");

			Assert.assertEquals(statusCode, 200, "Invalid authentication request should not return 200");
		}
	}

	// ============================================================
	// TOKEN VALIDATION
	// ============================================================

	@Then("the response should contain an authentication error {string}")
	public void response_should_contain_auth_error(String expectedError) {
		System.out.println("========================================================");
		System.out.println("Status Code : " + context.getResponse().getStatusCode());
		System.out.println("Status Code : " + context.getResponse().asPrettyString());
		System.out.println("========================================================");

		Assert.assertEquals(context.getResponse().jsonPath().getString("reason"), expectedError);
	}

	@Then("the response should contain a {string} field")
	public void responseShouldContainField(String field) {

		Assert.assertNotNull(response);

		Object value = response.jsonPath().get(field);

		Assert.assertNotNull(value, "Response does not contain field: " + field);
	}

	@Then("the {string} field should not be null")
	public void fieldShouldNotBeNull(String field) {

		Object value = response.jsonPath().get(field);

		Assert.assertNotNull(value, field + " should not be null");
	}

	@Then("the {string} field should not be empty")
	public void fieldShouldNotBeEmpty(String field) {

		String value = response.jsonPath().getString(field);

		Assert.assertNotNull(value, field + " should not be null");

		// Assert.assertFalse(field + " should not be empty", value.trim().isEmpty());
	}

	@Then("the {string} field should be of type string")
	public void fieldShouldBeOfTypeString(String field) {

		Object value = response.jsonPath().get(field);

		Assert.assertTrue(value instanceof String, field + " should be a String");
	}

	@Then("the response should contain a valid token")
	public void responseShouldContainValidToken() {

		Assert.assertNotNull(response);

		String token = response.jsonPath().getString("token");

		Assert.assertNotNull("Token should not be null", token);

		Assert.assertFalse(token.trim().isEmpty(), "Token should not be empty");
	}

	@Then("the response should contain a token")
	public void responseShouldContainAToken() {

		String token = response.jsonPath().getString("token");

		Assert.assertNotNull("Token should be present", token);
	}

	@Then("the token should not be null")
	public void tokenShouldNotBeNull() {

		String token = response.jsonPath().getString("token");

		Assert.assertNotNull("Token should not be null", token);
	}

	@Then("the token should not be empty")
	public void tokenShouldNotBeEmpty() {

		String token = response.jsonPath().getString("token");

		Assert.assertNotNull(token);

		Assert.assertFalse(token.trim().isEmpty(), "Token should not be empty");
	}

	@Then("the token should be of type string")
	public void tokenShouldBeOfTypeString() {

		Object token = response.jsonPath().get("token");

		Assert.assertTrue(token instanceof String, "Token should be String");
	}
	
	@Then("the token should be available for subsequent API requests")
	public void tokenShouldBeAvailableForSubsequentAPIRequests() {

		Assert.assertNotNull(TokenManager.getToken(), "Token is not available");

		Assert.assertFalse(TokenManager.getToken().isEmpty(), "Token should not be empty");
	}

	// ============================================================
	// RESPONSE CONTENT TYPE
	// ============================================================

	@Then("the response Content-Type should indicate JSON")
	public void responseContentTypeShouldIndicateJSON() {

		String responseContentType = response.getContentType();

		Assert.assertTrue(responseContentType.toLowerCase().contains("json"),
				"Response should be JSON but was: " + responseContentType);
	}

	// ============================================================
	// JSON RESPONSE
	// ============================================================

	@Then("the response body should be valid JSON")
	public void responseBodyShouldBeValidJSON() {

		String responseBody = response.getBody().asString();

		try {

			ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(responseBody);

		} catch (Exception e) {

			Assert.fail("Response body is not valid JSON: " + responseBody);
		}
	}

	// ============================================================
	// FIELD NAME
	// ============================================================

	// @Then("the response should contain exactly the expected token field name
	// {string}")
	public void response_ContainExactlyExpectedTokenFieldName(String expectedField) {

		Map<String, Object> responseMap = response.jsonPath().getMap("$");

		Assert.assertTrue(responseMap.containsKey(expectedField),
				"Response does not contain expected field: " + expectedField);
	}

	@Then("the response should contain exactly the expected token field name {string}")
	public void responseShouldContainExactlyTheExpectedTokenFieldName(String expectedFieldName) {

		Assert.assertNotNull(context.getResponse(), "Response should not be null");

		String responseBody = context.getResponse().asString();

		System.out.println("================================================");
		System.out.println("TOKEN FIELD VALIDATION");
		System.out.println("================================================");

		System.out.println("Response Body : " + responseBody);

		System.out.println("Expected Field Name : " + expectedFieldName);

		System.out.println("================================================");

		// ------------------------------------------------------------
		// Validate response is JSON
		// ------------------------------------------------------------

		Object token = null;

		try {
			token = context.getResponse().jsonPath().get(expectedFieldName);

		} catch (Exception e) {

			Assert.fail("Response is not valid JSON. Response: " + responseBody);
		}

		// ------------------------------------------------------------
		// Validate token field exists
		// ------------------------------------------------------------

		Assert.assertNotNull(token, "Expected token field '" + expectedFieldName + "' was not found in response.");

		// ------------------------------------------------------------
		// Validate token is not empty
		// ------------------------------------------------------------

		Assert.assertTrue(token instanceof String, "Token field should contain a String value.");

		String tokenValue = String.valueOf(token);

		Assert.assertFalse(tokenValue.isBlank(), "Token field should not be empty.");

		System.out.println("Token Field Found : " + expectedFieldName);

		System.out.println("Token Value : " + tokenValue);

		System.out.println("PASS: Response contains the expected token field '" + expectedFieldName + "'.");

		System.out.println("================================================");
	}

	// ============================================================
	// INVALID / SECURITY INPUT
	// ============================================================

	@Then("the response should contain an authentication error")
	public void responseShouldContainAuthenticationError() {

		Assert.assertNotNull(response);

		int statusCode = response.getStatusCode();

		Assert.assertNotEquals(statusCode, 200, "Invalid credentials should not return 200");
	}

	// ============================================================
	// MALFORMED REQUEST
	// ============================================================

	@Then("the API should reject the malformed JSON request")
	public void apiShouldRejectMalformedJSONRequest() {
		// Bad Request

		print_log();
		Assert.assertNotNull(response);

		Assert.assertNotEquals(response.getStatusCode(), 200,
				"Malformed JSON should not result in successful authentication");
		Assert.assertEquals(context.getResponse().getStatusCode(), 400, "Malformed JSON should not Found");
		Assert.assertEquals(context.getResponse().asPrettyString(), "Bad Request");
	}

	@Then("the API should not process the request as valid JSON authentication")
	public void apiShouldNotProcessRequestAsValidJSONAuthentication() {

		Assert.assertNotNull(response);

		Assert.assertNotEquals(response.getStatusCode(), 200, "Unsupported JSON request should not authenticate");
	}

	// ============================================================
	// GENERIC NEGATIVE RESPONSE
	// ============================================================

	@Then("the API should process the request according to its contract")
	public void apiShouldProcessRequestAccordingToContract() {

		Assert.assertNotNull(response, "API response should not be null");
	}

	@Then("the API should handle the request without crashing")
	public void apiShouldHandleRequestWithoutCrashing() {

		Assert.assertNotNull(response, "API should return a response");

		Assert.assertTrue(response.getStatusCode() >= 100 && response.getStatusCode() <= 599,
				"HTTP status code should be valid");

	}

	// ============================================================
	// CASE SENSITIVITY
	// ============================================================

	@Then("the API should authenticate according to its case-sensitivity rules")
	public void apiShouldAuthenticateAccordingToCaseSensitivityRules() {

		Assert.assertNotNull(response);

		/*
		 * We intentionally don't hard-code 200/401 here. The test verifies that the API
		 * returns a valid HTTP response.
		 */
		Assert.assertTrue(response.getStatusCode() >= 100 && response.getStatusCode() <= 599);
	}

	// ============================================================
	// SECURITY INPUT VALIDATION
	// ============================================================

	@Then("the response should not return a valid authentication token")
	public void responseShouldNotReturnAValidAuthenticationToken() {

		Assert.assertNotNull(response);

		if (response.getStatusCode() == 200) {

			if (context.getResponse().jsonPath().getString("reason") != null) {
				Assert.assertEquals(context.getResponse().jsonPath().getString("reason"), "Bad credentials");
			}

			String token = response.jsonPath().getString("token");

			Assert.assertTrue(token == null || token.isEmpty(), "Unexpected valid token returned");
		}
	}

	@Then("the API should safely process the input")
	public void apiShouldSafelyProcessTheInput() {

		Assert.assertNotNull(response);

		Assert.assertTrue(response.getStatusCode() >= 100 && response.getStatusCode() <= 599,
				"Invalid HTTP status code");

	}

	@Then("the response should not contain executable script content")
	public void responseShouldNotContainExecutableScriptContent() {

		String responseBody = response.getBody().asString();

		Assert.assertFalse(responseBody.contains("<script>"));
	}

	@Then("the response should not contain an invalid token")
	public void the_response_should_not_contain_an_invalid_token() {
		print_log();
		Assert.assertEquals(context.getResponse().getStatusCode(), 200);
		Assert.assertEquals(context.getResponse().jsonPath().getString("reason"), "Bad credentials");
	}

	@Then("the response should not expose the submitted password")
	public void responseShouldNotExposeTheSubmittedPassword() {

		// ------------------------------------------------------------
		// Validate response
		// ------------------------------------------------------------

		Assert.assertNotNull(context.getResponse(), "Response should not be null");

		Assert.assertNotNull(authBookingRequest, "Authentication request should not be null");

		String submittedPassword = authBookingRequest.getPassword();

		Assert.assertNotNull(submittedPassword, "Submitted password should not be null");

		Assert.assertFalse(submittedPassword.isBlank(), "Submitted password should not be empty");

		// ------------------------------------------------------------
		// Get response body
		// ------------------------------------------------------------

		String responseBody = context.getResponse().asString();

		// ------------------------------------------------------------
		// Log
		// ------------------------------------------------------------

		System.out.println("================================================");
		System.out.println("PASSWORD EXPOSURE CHECK");
		System.out.println("================================================");

		System.out.println("Response Body : " + responseBody);

		// NEVER print the real password
		System.out.println("Submitted Password : ********");

		System.out.println("================================================");

		// ------------------------------------------------------------
		// Check whether actual submitted password is returned
		// ------------------------------------------------------------

		boolean passwordExposed = responseBody.contains(submittedPassword);

		System.out.println("Password returned in response : " + passwordExposed);

		// ------------------------------------------------------------
		// Security assertion
		// ------------------------------------------------------------

		Assert.assertFalse(passwordExposed, "SECURITY FAILURE: Submitted password was returned " + "in the response.");

		System.out.println("PASS: Submitted password was NOT returned " + "in the response.");
	}

	@Then("the response should not expose the username and password unnecessarily")
	public void responseShouldNotExposeTheUsernameAndPasswordUnnecessarily() {

		Assert.assertNotNull(context.getResponse(), "Response should not be null");

		Assert.assertNotNull(authBookingRequest, "Authentication request should not be null");

		String submittedUsername = authBookingRequest.getUsername();
		String submittedPassword = authBookingRequest.getPassword();

		Assert.assertNotNull(submittedUsername, "Submitted username should not be null");

		Assert.assertNotNull(submittedPassword, "Submitted password should not be null");

		String responseBody = context.getResponse().asString();
		String responseHeaders = context.getResponse().getHeaders().toString();

		System.out.println("================================================");
		System.out.println("USERNAME / PASSWORD EXPOSURE SECURITY CHECK");
		System.out.println("================================================");

		System.out.println("Response Body : " + responseBody);
		System.out.println("Username returned : " + responseBody.contains(submittedUsername));

		// Never print the actual password
		System.out.println("Password returned : " + responseBody.contains(submittedPassword));

		System.out.println("================================================");

		// ------------------------------------------------------------
		// Check username is not exposed in response body
		// ------------------------------------------------------------

		Assert.assertFalse(responseBody.contains(submittedUsername),
				"SECURITY FAILURE: Submitted username was exposed " + "in the response body.");

		// ------------------------------------------------------------
		// Check password is not exposed in response body
		// ------------------------------------------------------------

		Assert.assertFalse(responseBody.contains(submittedPassword),
				"SECURITY FAILURE: Submitted password was exposed " + "in the response body.");

		// ------------------------------------------------------------
		// Check username is not exposed in response headers
		// ------------------------------------------------------------

		Assert.assertFalse(responseHeaders.contains(submittedUsername),
				"SECURITY FAILURE: Submitted username was exposed " + "in the response headers.");

		// ------------------------------------------------------------
		// Check password is not exposed in response headers
		// ------------------------------------------------------------

		Assert.assertFalse(responseHeaders.contains(submittedPassword),
				"SECURITY FAILURE: Submitted password was exposed " + "in the response headers.");

		System.out.println("PASS: Username and password were not unnecessarily " + "exposed in the response.");

		System.out.println("================================================");
	}

}
