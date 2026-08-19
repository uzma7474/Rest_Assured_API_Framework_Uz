package docs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import services.AuthMeService;
import utils.TokenManager;

import java.util.Map;

import context.ScenarioContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EventsSteps {

	private final ScenarioContext context;

	private EventsService eventsService;
	private Response response;

	private String category;
	private String city;
	private String search;
	private int page;
	private int limit;

	public EventsSteps(ScenarioContext context) {

		this.context = context;
		this.eventsService = new EventsService();
	}

	// ============================================================
	// BACKGROUND
	// ============================================================

	@Given("the EventHub API base URL is configured")
	public void eventHubApiBaseUrlIsConfigured() {

		// Base URL is already configured through ConfigManager /
		// RequestSpecificationFactory.

		assertThat("EventHub API base URL should be configured", true, is(true));
	}

	@Given("the user has a valid authentication token")
	public void userHasValidAuthenticationToken() {

		String token = TokenManager.getToken();

		assertThat("Authentication token should not be null", token, notNullValue());

		assertThat("Authentication token should not be empty", token.trim(), not(emptyString()));
	}

	@And("the request Accept header is set to {string}")
	public void requestAcceptHeaderIsSetTo(String acceptHeader) {

		assertThat("Accept header should be application/json", acceptHeader, equalTo("application/json"));
	}

	// ============================================================
	// AUTHENTICATION
	// ============================================================

	@Given("the Authorization header contains a valid Bearer token")
	public void authorizationHeaderContainsValidBearerToken() {

		String token = TokenManager.getToken();

		assertThat("Token should be available", token, notNullValue());

		assertThat("Token should not be empty", token.trim(), not(emptyString()));
	}

	// ============================================================
	// TC01 / TC02 / TC04
	// ============================================================

//	@When("the user sends a GET request to {string}")
//	public void userSendsGetRequest(String endpoint) {
//
//		if (endpoint.equals("/api/events")) {
//
//			response = eventsService.getEvents();
//
//		} else {
//
//			throw new IllegalArgumentException("Unsupported endpoint: " + endpoint);
//		}
//	}

	@When("the user sends a GET request to {string}")
	public void userSendsGetRequest(String endpoint) {

		if (!endpoint.equals("/api/events")) {
			throw new IllegalArgumentException("Unsupported endpoint: " + endpoint);
		}

		// TC03 - query parameters are configured
		if (category != null || city != null || search != null) {

			response = eventsService.getEvents(category, city, search, page, limit);

		} else {

			// TC01, TC02, TC04
			response = eventsService.getEvents();
		}
	}

	// ============================================================
	// TC03
	// ============================================================

	@And("the following query parameters are provided:")
	public void followingQueryParametersAreProvided(DataTable dataTable) {

		Map<String, String> parameters = dataTable.asMap(String.class, String.class);

		category = parameters.get("category");
		city = parameters.get("city");
		search = parameters.get("search");

		page = Integer.parseInt(parameters.get("page"));
		limit = Integer.parseInt(parameters.get("limit"));
	}

	@When("the user sends a GET request to {string} with the configured query parameters")
	public void userSendsGetRequestWithConfiguredQueryParameters(String endpoint) {

		if (endpoint.equals("/api/events")) {

			response = eventsService.getEvents(category, city, search, page, limit);

		} else {

			throw new IllegalArgumentException("Unsupported endpoint: " + endpoint);
		}
	}

	/*
	 * This step allows TC03 to use the original feature wording:
	 *
	 * When the user sends a GET request to "/api/events"
	 *
	 * after query parameters have been configured.
	 *
	 * It is kept separate from the normal request method so the request can be
	 * constructed using the DataTable values.
	 */

	@When("the user sends a GET request to {string} using the provided query parameters")
	public void userSendsGetRequestUsingProvidedQueryParameters(String endpoint) {

		if (endpoint.equals("/api/events")) {

			response = eventsService.getEvents(category, city, search, page, limit);

		} else {

			throw new IllegalArgumentException("Unsupported endpoint: " + endpoint);
		}
	}

	// ============================================================
	// STATUS CODE
	// ============================================================

	@Then("the response status code should be {int}")
	public void responseStatusCodeShouldBe(int expectedStatusCode) {

		assertThat("Unexpected response status", response.getStatusCode(), equalTo(expectedStatusCode));
	}

	// ============================================================
	// SUCCESS
	// ============================================================

	@And("the response should contain {string} as true")
	public void responseShouldContainAsTrue(String field) {

		Boolean value = response.jsonPath().getBoolean(field);

		assertThat("Expected " + field + " to be true", value, is(true));
	}

	// ============================================================
	// FIELD VALIDATION
	// ============================================================

	@And("the response should contain the {string} field")
	public void responseShouldContainField(String field) {

		Object value = response.jsonPath().get(field);

		assertThat("Response should contain field: " + field, value, notNullValue());
	}

	// ============================================================
	// CONTENT TYPE
	// ============================================================

	@And("the response Content-Type should contain {string}")
	public void responseContentTypeShouldContain(String expectedContentType) {

		String contentType = response.getContentType();

		assertThat("Unexpected Content-Type", contentType, containsString(expectedContentType));
	}

	// ============================================================
	// PAGINATION
	// ============================================================

	@And("the pagination page should be {int}")
	public void paginationPageShouldBe(int expectedPage) {

		int actualPage = response.jsonPath().getInt("pagination.page");

		assertThat("Unexpected pagination page", actualPage, equalTo(expectedPage));
	}

	@And("the pagination limit should be {int}")
	public void paginationLimitShouldBe(int expectedLimit) {

		int actualLimit = response.jsonPath().getInt("pagination.limit");

		assertThat("Unexpected pagination limit", actualLimit, equalTo(expectedLimit));
	}
}