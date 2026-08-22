package services;

import static io.restassured.RestAssured.given;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.BookingBaseApi;
import endpoints.AuthEndpoints;
import endpoints.BookingEndpoints;
import io.restassured.http.ContentType;
import io.restassured.internal.util.IOUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.requests.AuthBookingRequest;
import models.requests.RegisterRequest;
import models.response.AuthBookingResponse;
import models.response.RegisterResponse;

public class BookingService extends BookingBaseApi {

	private RequestSpecification requestSpecification;
	public AuthBookingRequest authBookingRequest;
	//private RequestSpecification reqSpecWithTextContentType;
	private ContentType contentType;

	public BookingService() {
		this.requestSpecification = getBookingRequestSpec();
		//this.reqSpecWithTextContentType = getBookingRequestSpecGivenContentType(ContentType.TEXT);

		// this.registerRequest = new
		// RegisterRequest(ConfigManager.getProperty("email"),
		// ConfigManager.getProperty("password"));
		this.authBookingRequest = new AuthBookingRequest();
	}

	/**
	 * Create a token for a new user.
	 *
	 * @param AuthBookingRequest payload
	 * @return API response
	 */
	public Response createBookingToken(AuthBookingRequest authBookingRequest) {

		return given()
					.header("Content-Type", "application/json; charset=UTF-8")
					.spec(requestSpecification)
					.body(authBookingRequest)
				.when()
					.post(BookingEndpoints.CREATE_TOKEN);
	}
	
	public Response createBookingTokenTekeObject(Object authBookingRequest) {

		return given()
					.header("Content-Type", "application/json; charset=UTF-8")
					.spec(requestSpecification)
					.body(authBookingRequest)
				.when()
					.post(BookingEndpoints.CREATE_TOKEN);
	}

	public Response createTokenWithRawJson(String requestBody) {

	    return given()
			            .spec(requestSpecification)
			            .contentType("application/json")
			            .body(requestBody)
			    .when()
			            .post(BookingEndpoints.CREATE_TOKEN);
	}



	public Response createBookingTokenWithContentType(AuthBookingRequest authBookingRequest, String contentTypeValue) {

	    return given()
	            .spec(requestSpecification)
	            .contentType(contentTypeValue)
	            .accept(ContentType.JSON)
	            .body(authBookingRequest)

	        .when()
	            .post(BookingEndpoints.CREATE_TOKEN)

	        .then()
	            .extract()
	            .response();
	}


	public Response createBookingTokenWithRawJsonAndContentType(String requestBody, String contentTypeValue) {

	    return given()
	            .spec(requestSpecification)
	            .contentType(contentTypeValue)
	            .accept(ContentType.JSON)
	            .body(requestBody)
	            .log()
	            .all()

	        .when()
	            .post(BookingEndpoints.CREATE_TOKEN)

	        .then()
	            .log()
	            .all()
	            .extract()
	            .response();
	}
	
	public Response createTokenWithoutBody(String contentTypeValue) {

		return given()
					.header("Content-Type", contentTypeValue)
					.spec(requestSpecification)
					.body("")
				.when()
					.post(BookingEndpoints.CREATE_TOKEN);
	}
	
	
	public Response createBookingTokenWithContentType(String authEndpoint, String contentType, AuthBookingRequest requestBody) {

	    String jsonBody;

	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        jsonBody = objectMapper.writeValueAsString(requestBody);

	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Failed to serialize authentication request body to JSON", e  );
	    }

	    return given()
	            .spec(requestSpecification)
	            .header("Content-Type", contentType)
	            .body(jsonBody)
	            .when()
	            .post(authEndpoint);
	}
	
	

	// sendRequest
	public Response sendRequest(String httpMethod, AuthBookingRequest authBookingRequest, String contentTypeValue) {
		String method = httpMethod.toUpperCase();
		Response response = null;

		switch (method) {
		case "GET":
			response = given()
							.header("Content-Type", contentTypeValue)
							.spec(requestSpecification)
							.body(authBookingRequest)
						.when()
							.get(BookingEndpoints.CREATE_TOKEN);
			break;
		case "PUT":
			response = given()
							.header("Content-Type", contentTypeValue)
							.spec(requestSpecification)
							.body(authBookingRequest)
						.when()
							.put(BookingEndpoints.CREATE_TOKEN);
			break;
		case "PATCH":
			response = given()
							.header("Content-Type", contentTypeValue)
							.spec(requestSpecification)
							.body(authBookingRequest)
						.when()
							.patch(BookingEndpoints.CREATE_TOKEN);
			break;
			
		case "DELETE":
			response = given()
							.header("Content-Type", contentTypeValue)
							.spec(requestSpecification)
							.body(authBookingRequest)
						.when()
							.delete(BookingEndpoints.CREATE_TOKEN);
			break;

		}
		return response;
	}

	public AuthBookingResponse tokenCreationUsingPojo(Response res) {
		Response response = res.then()
									.extract()
									.response();

		if (response.statusCode() == 200) {
			return response.as(AuthBookingResponse.class);
		}

		return null;

	}
	
	public Response createTokenWithRawBody(String authEndpoint, String contentType, String rawRequestBody) {

	    return given()
	            .spec(requestSpecification)
	            .header("Content-Type", contentType)
	            .body(rawRequestBody)
	            .when()
	            .post(authEndpoint);
	}

	public void sendRequestWithTextContentType() {
		
	}
	
	
	
	
}
