package base;

import config.ConfigManager;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

/**
 * ResponseSpecificationFactory
 *
 * Centralized factory for creating REST Assured ResponseSpecifications.
 *
 * Responsibilities: - Common response content type - Common response logging -
 * Common response time validation - Reusable response specifications
 */
public final class ResponseSpecificationFactory {

	private ResponseSpecificationFactory() {
		// Prevent object creation
	}

	/**
	 * Creates the default response specification.
	 *
	 * This specification validates that the API returns JSON content.
	 *
	 * @return ResponseSpecification
	 */
	public static ResponseSpecification createDefaultResponseSpecification() {

		ResponseSpecBuilder builder = new ResponseSpecBuilder();

		builder.expectContentType(ContentType.JSON);

		return builder.build();
	}

	/**
	 * Creates a response specification with response logging.
	 *
	 * Useful for debugging API failures.
	 *
	 * @return ResponseSpecification
	 */
	public static ResponseSpecification createLoggingResponseSpecification() {

		return new ResponseSpecBuilder()

				.expectContentType(ContentType.JSON)

				 .log(LogDetail.ALL)

				.build();
	}

	/**
	 * Creates a response specification with response time validation.
	 *
	 * Default response time: 5000 milliseconds.
	 *
	 * @return ResponseSpecification
	 */
	public static ResponseSpecification createPerformanceResponseSpecification() {

		long maxResponseTime = ConfigManager.getIntProperty("max.response.time", 5000);

		return new ResponseSpecBuilder()

				.expectContentType(ContentType.JSON)

				.expectResponseTime(org.hamcrest.Matchers.lessThan(maxResponseTime),
						java.util.concurrent.TimeUnit.MILLISECONDS)

				.build();
	}
}
