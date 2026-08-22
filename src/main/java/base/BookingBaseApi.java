package base;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import utils.TokenManager;

public class BookingBaseApi {
	
	protected RequestSpecification requestSpec;
	protected RequestSpecification reqSpecGivenContentType;
	protected ContentType contentType;

	//protected ResponseSpecification responseSpec;
	
	//protected RequestSpecification requestSpecTextContent;
	
	/**
	 * Constructor.
	 *
	 * Initializes request and response specifications.
	 */
	public BookingBaseApi() {

		this.requestSpec = RequestSpecificationFactory.createApiRequestSpecificationForBooking();
		//this.reqSpecGivenContentType = RequestSpecificationFactory.createApiRequestSpecForBookingGivenContentType(contentType);

	}
	
	/**
	 * Returns the default request specification.
	 *
	 * Used for APIs that do not require authentication.
	 *
	 * @return RequestSpecification
	 */
	protected RequestSpecification getBookingRequestSpec() {

		return given().spec(requestSpec);
	}
	
	
	protected RequestSpecification getBookingRequestSpecGivenContentType(ContentType text) {

		return given().spec(reqSpecGivenContentType);
	}
	
	
	
	
	/**
	 * Returns an authenticated request specification.
	 *
	 * @return authenticated RequestSpecification
	 */
	protected RequestSpecification getBookingAuthenticatedRequestSpec() {

		String token = TokenManager.getToken();

		if (token == null || token.isBlank()) {

			throw new IllegalStateException("Authentication token is null or empty. "
					+ "Please login before calling an " + "authenticated API.");
		}

		return given()
				.spec(requestSpec)
				.header("Authorization", "Basic " + token);
	}


	/**
	 * Sets the base URI globally.
	 *
	 * @param baseUri API base URI
	 */
	protected void setBaseUri(String baseUri) {

		RestAssured.baseURI = baseUri;
	}

	/**
	 * Clears the globally configured REST Assured base URI.
	 */
	protected void resetBaseUri() {

		RestAssured.reset();
	}
	
	

}
