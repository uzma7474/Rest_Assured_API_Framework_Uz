package base;

import config.ConfigManager;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.restassured.RestAssured;
import utils.TokenManager;

/**
 * BaseTest
 *
 * Base class for API tests.
 *
 * Responsibilities: - Initialize REST Assured configuration - Load environment
 * configuration - Configure base URI - Perform framework-level cleanup
 *
 * Note: Cucumber scenarios should primarily use ApiHooks for scenario-level
 * setup and cleanup.
 */
public class BaseTest {

	/**
	 * Runs once before the entire TestNG suite.
	 */
	@BeforeAll()
	public void beforeSuite() {

		String baseUrl = ConfigManager.getProperty("base.url");

		if (baseUrl == null || baseUrl.isBlank()) {

			throw new IllegalStateException("base.url is not configured.");
		}

		RestAssured.baseURI = baseUrl;

		System.out.println("==========================================");

		System.out.println("REST Assured Test Suite Started");

		System.out.println("Environment: " + ConfigManager.getEnvironment());

		System.out.println("Base URL: " + baseUrl);

		System.out.println("==========================================");
	}

	/**
	 * Runs once after the entire TestNG suite.
	 */
	@AfterAll()
	public void afterSuite() {

		TokenManager.clear();

		RestAssured.reset();

		System.out.println("==========================================");

		System.out.println("REST Assured Test Suite Finished");

		System.out.println("==========================================");
	}
}
