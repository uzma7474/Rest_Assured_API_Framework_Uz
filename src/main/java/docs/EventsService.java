package docs;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.TokenManager;

import static io.restassured.RestAssured.given;

import base.BaseApi;

public class EventsService extends BaseApi {

	private static final String EVENTS_ENDPOINT = "/api/events";

	/**
	 * TC01 / TC02 / TC04
	 */
	public Response getEvents() {

		return given()
					.spec(getRequestSpec())
					.header("Accept", "application/json")
				.when()
					.get(EventsEnpoints.EVENTS_LIST)
				.then()
					.extract()
					.response();
	}

	/**
	 * TC03
	 */
	public Response getEvents(String category, String city, String search, int page, int limit) {

		return given()
					.spec(getRequestSpec())
					.header("Accept", "application/json")
					.queryParam("category", category)
					.queryParam("city", city)
					.queryParam("search", search)
					.queryParam("page", page)
					.queryParam("limit", limit)
				.when()
					.get(EventsEnpoints.EVENTS_LIST)
				.then()
					.extract()
					.response();
	}

	/**
	 * Get events with optional query parameters. Null or empty values are not added
	 * to the request.
	 */
	public Response getEventsWithOptionalParameters(String category, String city, String search, Integer page, Integer limit) {

		RequestSpecification request = given()
										.spec(getAuthenticatedRequestSpec())
										.header("Accept", "application/json");

		if (category != null && !category.isBlank()) {
			request.queryParam("category", category);
		}

		if (city != null && !city.isBlank()) {
			request.queryParam("city", city);
		}

		if (search != null && !search.isBlank()) {
			request.queryParam("search", search);
		}

		if (page != null) {
			request.queryParam("page", page);
		}

		if (limit != null) {
			request.queryParam("limit", limit);
		}

		return request
				.when()
					.get(EVENTS_ENDPOINT)
				.then()
					.extract()
					.response();
	}

	/**
	 * Create authenticated request specification.
	 */
	public RequestSpecification getAuthenticatedRequestSpec() {

		return given()
				.header("Authorization", "Bearer " + TokenManager.getToken());
	}

}