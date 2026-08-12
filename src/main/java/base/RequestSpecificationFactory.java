package base;

import config.ConfigManager;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

//Core REST Assured methods (given(), when(), get(), post(), etc.)
import static io.restassured.RestAssured.*;

//Matchers for validating responses (equalTo(), hasItem(), containsString(), etc.)
import static org.hamcrest.Matchers.*;

//REST Assured specific matchers (e.g., matchesXsd() for XML validation)
import static io.restassured.matcher.RestAssuredMatchers.*;

/**
 * RequestSpecificationFactory
 *
 * Responsible for creating REST Assured RequestSpecifications.
 *
 * Benefits: - Centralized API configuration - Environment-specific base URL -
 * Common headers - Content type - Timeout configuration - Logging configuration
 */
public final class RequestSpecificationFactory {

	private RequestSpecificationFactory() {
		// Prevent object creation
	}

	/**
	 * Creates the default RequestSpecification.
	 *
	 * @return configured RequestSpecification
	 */
	public static RequestSpecification createDefaultRequestSpecification() {

		String baseUrl = ConfigManager.getProperty("base.url");

		int requestTimeout = ConfigManager.getIntProperty("request.timeout", 30000);

		int connectionTimeout = ConfigManager.getIntProperty("connection.timeout", 30000);

		int socketTimeout = ConfigManager.getIntProperty("socket.timeout", 30000);
		
		// 1. Instantiate the authentication scheme
		PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
		authScheme.setUserName(ConfigManager.getProperty("email"));
		authScheme.setPassword(ConfigManager.getProperty("password"));
		
		

		RequestSpecBuilder builder = new RequestSpecBuilder()
			
				.setBaseUri(baseUrl)
				
				//.setAuth(authScheme)

				.setContentType(ContentType.JSON)

				.setAccept(ContentType.JSON)

				.setConfig(RestAssuredConfig.config()
						.httpClient(HttpClientConfig.httpClientConfig()
								.setParam("http.connection.timeout", connectionTimeout)
								.setParam("http.socket.timeout", socketTimeout)
								.setParam("http.connection-manager.timeout", requestTimeout)));

		return builder.build();
	}
	
	public static  RequestSpecification createRequestSpecificationOfTextContent() {
		String baseUrl = ConfigManager.getProperty("base.url");

		int requestTimeout = ConfigManager.getIntProperty("request.timeout", 30000);

		int connectionTimeout = ConfigManager.getIntProperty("connection.timeout", 30000);

		int socketTimeout = ConfigManager.getIntProperty("socket.timeout", 30000);
		
		// 1. Instantiate the authentication scheme
		PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
		authScheme.setUserName(ConfigManager.getProperty("email"));
		authScheme.setPassword(ConfigManager.getProperty("password"));
		
		

		RequestSpecBuilder builder = new RequestSpecBuilder()
			
				.setBaseUri(baseUrl)
				
				//.setAuth(authScheme)

				.setContentType(ContentType.TEXT)

				.setAccept(ContentType.JSON)

				.setConfig(RestAssuredConfig.config()
						.httpClient(HttpClientConfig.httpClientConfig()
								.setParam("http.connection.timeout", connectionTimeout)
								.setParam("http.socket.timeout", socketTimeout)
								.setParam("http.connection-manager.timeout", requestTimeout)));

		return builder.build();
		
		
	}

	/**
	 * Creates a request specification with logging.
	 *
	 * Useful for debugging API requests.
	 *
	 * @return RequestSpecification
	 */
	public static RequestSpecification createLoggingRequestSpecification() {

		return new RequestSpecBuilder()

				.setBaseUri(ConfigManager.getProperty("base.url"))

				.setContentType(ContentType.JSON)

				.setAccept(ContentType.JSON)

				 .log(LogDetail.ALL)

				.build();
	}

	/**
	 * Creates an API request specification with common headers.
	 *
	 * @return RequestSpecification
	 */
	public static RequestSpecification createApiRequestSpecification() {

		return new RequestSpecBuilder()

				.setBaseUri(ConfigManager.getProperty("base.url"))

				.setContentType(ContentType.JSON)

				.setAccept(ContentType.JSON)

				.addHeader("X-API-Version", ConfigManager.getProperty("api.version", "v1"))

				.build();
	}
}
