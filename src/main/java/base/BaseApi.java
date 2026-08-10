
package base;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import utils.TokenManager;

/**
 * BaseApi
 *
 * Common parent class for API services.
 *
 * Responsibilities: - Provides common REST Assured configuration - Creates
 * request specifications - Creates response specifications - Provides
 * authenticated request specifications - Provides unauthenticated request
 * specifications
 */
public class BaseApi {

	protected RequestSpecification requestSpec;

	protected ResponseSpecification responseSpec;
	


	/**
	 * Constructor.
	 *
	 * Initializes request and response specifications.
	 */
	public BaseApi() {

		this.requestSpec = RequestSpecificationFactory.createDefaultRequestSpecification();

		this.responseSpec = ResponseSpecificationFactory.createDefaultResponseSpecification();
	}

	/**
	 * Returns the default request specification.
	 *
	 * Used for APIs that do not require authentication.
	 *
	 * @return RequestSpecification
	 */
	protected RequestSpecification getRequestSpec() {

		return given().spec(requestSpec);
	}

	/**
	 * Returns an authenticated request specification.
	 *
	 * @return authenticated RequestSpecification
	 */
	protected RequestSpecification getAuthenticatedRequestSpec() {

		String token = TokenManager.getToken();

		if (token == null || token.isBlank()) {

			throw new IllegalStateException("Authentication token is null or empty. "
					+ "Please login before calling an " + "authenticated API.");
		}

		return given().spec(requestSpec).header("Authorization", "Bearer " + token);
	}

	/**
	 * Creates a JSON request specification.
	 *
	 * @return RequestSpecification
	 */
	protected RequestSpecification getJsonRequestSpec() {

		return given().spec(requestSpec).contentType(ContentType.JSON).accept(ContentType.JSON);
	}

	/**
	 * Creates an authenticated JSON request specification.
	 *
	 * @return RequestSpecification
	 */
	protected RequestSpecification getAuthenticatedJsonRequestSpec() {

		String token = TokenManager.getToken();

		if (token == null || token.isBlank()) {

			throw new IllegalStateException("Authentication token is null or empty.");
		}

		return given().spec(requestSpec).contentType(ContentType.JSON).accept(ContentType.JSON).header("Authorization",
				"Bearer " + token);
	}

	/**
	 * Returns the default response specification.
	 *
	 * @return ResponseSpecification
	 */
	protected ResponseSpecification getResponseSpec() {

		return responseSpec;
	}

	/**
	 * Returns a logging response specification.
	 *
	 * Useful for debugging API responses.
	 *
	 * @return ResponseSpecification
	 */
	protected ResponseSpecification getLoggingResponseSpec() {

		return ResponseSpecificationFactory.createLoggingResponseSpecification();
	}

	/**
	 * Returns a performance response specification.
	 *
	 * @return ResponseSpecification
	 */
	protected ResponseSpecification getPerformanceResponseSpec() {

		return ResponseSpecificationFactory.createPerformanceResponseSpecification();
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
