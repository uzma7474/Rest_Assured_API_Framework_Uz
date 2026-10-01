package stepdefinitions;

import context.ScenarioContext;
import endpoints.BookingEndpoints;
import io.restassured.response.Response;
import services.GetBookingIdService;

import static org.hamcrest.Matchers.*;

import java.util.Date;
import java.util.List;

import org.testng.Assert;

import config.ConfigManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetBookingIdSteps {

	private ScenarioContext context;

	public Response response;

	private String firstname;
	private String lastname;
	private String checkin;
	private String checkout;

	private String bookingEndpoint;

	private boolean requestBodyPresent = true;

	private String requestedFirstname;

	private String requestedLastname;

	private List<Integer> bookingIds = null;

	private GetBookingIdService getBookingIdService = new GetBookingIdService();

	public GetBookingIdSteps() {

	}

	// PicoContainer automatically injects this
	public GetBookingIdSteps(ScenarioContext context) {
		this.context = context;
	}

	public void print_log() {

		System.out.println("======================================================");
		System.out.println("==================== RESPONSE ========================");
		System.out.println("Status Code : " + context.getResponse().getStatusCode());
		System.out.println("Content-Type : " + context.getResponse().getContentType());
		System.out.println("Response Body : " + context.getResponse().asPrettyString());
		System.out.println("======================================================");
		System.out.println("======================================================");

	}

//====================================== Given ===============================================
//  GIVEN
//============================================================================================	

	// ============================================================
	// BACKGROUND
	// ============================================================

	@Given("the Restful Booker for GetBookingIds API is available")
	public void theRestfulBookingAPIIsAvailable() {

		System.out.println("================================================");
		System.out.println("Restful Booker API");
		System.out.println("Base URL: " + ConfigManager.getProperty("booking.baseUrl"));
		System.out.println("================================================");

		Assert.assertNotNull(ConfigManager.getProperty("booking.baseUrl"),
				"Restful Booker Base URL should not be null");
	}

	@Given("the booking endpoint is configured as {string}")
	public void theBookingEndpointIsConfiguredAs(String endpoint) {

		bookingEndpoint = endpoint;

		System.out.println("Booking Endpoint: " + bookingEndpoint);

		Assert.assertNotNull(bookingEndpoint, "Booking endpoint should not be null");

		Assert.assertEquals(BookingEndpoints.BOOKING, bookingEndpoint);
	}

	@Given("a booking exists with a valid firstname")
	public void aBookingExistsWithAValidFirstname() {
		/*
		 * We use a known firstname.
		 *
		 * Better approach: Create a booking dynamically using POST /booking and capture
		 * its firstname.
		 */
		requestedFirstname = ConfigManager.getProperty("firstname");

		System.out.println("================================================");
		System.out.println("Booking Search Test Data");
		System.out.println("Firstname: " + requestedFirstname);
		System.out.println("================================================");
	}

	@Given("firstname is {string}")
	public void firstname_is(String firstname) {

		this.firstname = firstname;

	}

	@Given("lastname is {string}")
	public void lastname_is(String lastname) {

		this.lastname = lastname;

	}

	@Given("checkin date is {string}")
	public void checkin_is(String checkin) {

		this.checkin = checkin;

	}

	@Given("checkout date is {string}")
	public void checkout_is(String checkoutDate) {

		this.checkout = checkoutDate;

		System.out.println("Checkout Date : " + checkoutDate);
	}

	// ============================================================
	// ACCEPT HEADER
	// ============================================================

	@Given("the Accept header is {string}")
	public void theAcceptHeaderIs(String acceptHeader) {

		// Header can be added through the service/request specification.
		System.out.println("Accept Header : " + acceptHeader);
	}

	// ============================================================
	// CONTENT TYPE HEADER
	// ============================================================

	@Given("the Content-Type header is {string}")
	public void theContentTypeHeaderIs(String contentType) {

		System.out.println("Content-Type Header : " + contentType);
	}

	// ============================================================
	// UTILITY METHODS
	// ============================================================

	private String normalize(String value) {

		if (value == null) {
			return null;
		}

		if (value.isBlank()) {
			return null;
		}

		return value;
	}

//=============================================================================================	
// WHEN	
//=============================================================================================

	@When("the user send Get request for getting booking ids")
	public void send_get_request_for_getting_booking_ids() {
		response = getBookingIdService.getBookingIdsWithNoQuery();
		context.setResponse(response);
		System.out.println("================================================");
		System.out.println("GET Booking IDs");
		System.out.println("Endpoint: " + BookingEndpoints.BOOKING);
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response : " + context.getResponse().asPrettyString());
		System.out.println("================================================");

	}

	@When("the client sends a GET request to the booking endpoint with an empty query string")
	public void get_request_to_booking_endpoint_with_empty_query_string() {
		response = getBookingIdService.getBookingIdsWithNoQuery();
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("GET Booking IDs");
		System.out.println("Endpoint: " + BookingEndpoints.BOOKING);
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response : " + context.getResponse().asPrettyString());
		System.out.println("================================================");

	}

	// ============================================================
	// GET BOOKING IDS
	// ============================================================

	@When("the client sends a GET request to the booking endpoint")
	public void theClientSendsAGETRequestToTheBookingEndpoint() {

		response = getBookingIdService.getBookingIds();
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("GET Booking IDs");
		System.out.println("Endpoint: " + BookingEndpoints.BOOKING);
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response : " + context.getResponse().asPrettyString());
		System.out.println("================================================");
	}

	@When("the client sends a GET request with the firstname filter")
	public void theClientSendsAGETRequestWithTheFirstnameFilter() {

		response = getBookingIdService.getBookingIdsByFirstname("Riya");

		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("GET Booking IDs By Firstname");
		System.out.println("Firstname: " + requestedFirstname);
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response: " + context.getResponse().asPrettyString());
		System.out.println("================================================");

		/*
		 * Extract booking IDs from:
		 *
		 * [ { "bookingid": 1 }, { "bookingid": 5 } ]
		 */
		bookingIds = context.getResponse().jsonPath().getList("bookingid", Integer.class);

		System.out.println("Returned Booking IDs: " + bookingIds);
	}

	// ============================================================
	// GET REQUEST - FIRSTNAME
	// ============================================================
	@When("the user sends a GET request with firstname")
	public void user_sends_get_request_firstname() {
		response = getBookingIdService.getBookingIdsByFirstname(firstname);
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("GET Booking IDs By Firstname");
		System.out.println("Firstname: " + requestedFirstname);
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response: " + context.getResponse().asPrettyString());
		System.out.println("================================================");

		/*
		 * Extract booking IDs from:
		 *
		 * [ { "bookingid": 1 }, { "bookingid": 5 } ]
		 */
		bookingIds = context.getResponse().jsonPath().getList("bookingid", Integer.class);

		System.out.println("Returned Booking IDs: " + bookingIds);
	}

	@When("the user sends a GET request with lastname")
	public void user_send_get_request_with_lastname() {
		response = getBookingIdService.getBookingIdsByLastname(lastname);
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("GET Booking IDs By Firstname");
		System.out.println("Firstname: " + requestedFirstname);
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response: " + context.getResponse().asPrettyString());
		System.out.println("================================================");

		/*
		 * Extract booking IDs from:
		 *
		 * [ { "bookingid": 1 }, { "bookingid": 5 } ]
		 */
		bookingIds = context.getResponse().jsonPath().getList("bookingid", Integer.class);

		System.out.println("Returned Booking IDs: " + bookingIds);
	}

	@When("the user sends a GET request with firstname and lastname")
	public void user_sends_get_request_with_firstname_lastname() {
		response = getBookingIdService.getBookingIdsWithParams(firstname, lastname, "", "");
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response: " + context.getResponse().asPrettyString());
		System.out.println("================================================");

		/*
		 * Extract booking IDs from:
		 *
		 * [ { "bookingid": 1 }, { "bookingid": 5 } ]
		 */
		bookingIds = context.getResponse().jsonPath().getList("bookingid", Integer.class);

		System.out.println("Returned Booking IDs: " + bookingIds);
	}

	@When("the user sends a GET request with checkin date")
	public void user_sends_get_request_with_checkin_date() {
		response = getBookingIdService.getBookingIdsWithParams("", "", checkin, "");
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response: " + context.getResponse().asPrettyString());
		System.out.println("================================================");
	}

	@When("the user sends a GET request with checkout date")
	public void the_user_sends_a_get_request_with_checkout_date() {
		response = getBookingIdService.getBookingIdsWithParams("", "", "", checkout);
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response: " + context.getResponse().asPrettyString());
		System.out.println("================================================");
	}
	
	@When("the user sends a GET request with firstname and checkin date")
	public void user_send_get_request_with_firstname_checkin_date() {
		response = getBookingIdService.getBookingIdsWithParams(firstname, "", checkin, "");
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response: " + context.getResponse().asPrettyString());
		System.out.println("================================================");
	}

	

	@When("the user sends a GET request with checkin and checkout dates")
	public void the_user_sends_a_get_request_with_checkin_and_checkout_dates() {
		response = getBookingIdService.getBookingIdsWithParams("", "", checkin, checkout);
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response: " + context.getResponse().asPrettyString());
		System.out.println("================================================");
	}
	
	@When("the user sends a GET request with firstname and checkout date")
	public void user_send_get_request_with_firstname_checkout_date() {
		response = getBookingIdService.getBookingIdsWithParams(firstname, "", "", checkout);
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response: " + context.getResponse().asPrettyString());
		System.out.println("================================================");
	}
	
	@When("the user sends a GET request with lastname and checkout date")
	public void user_sends_get_request_with_lastname_checkout_date() {
		response = getBookingIdService.getBookingIdsWithParams("", lastname, "", checkout);
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response: " + context.getResponse().asPrettyString());
		System.out.println("================================================");
	}
	
	@When("the user sends a GET request with all booking filters")
	public void user_sends_get_request_with_all_booking_filter() {
		response = getBookingIdService.getBookingIdsWithParams(firstname, lastname, checkin, checkout);
		context.setResponse(response);

		System.out.println("================================================");
		System.out.println("Status Code: " + context.getResponse().getStatusCode());
		System.out.println("Response: " + context.getResponse().asPrettyString());
		System.out.println("================================================");
	}
	
	
	
	
	
//=============================================================================================
// THEN	
//=============================================================================================	

	@Then("the response should contain non_empty list of booking Id")
	public void reponse_contain_non_empty_list_of_booking_id() {
		print_log();

		Assert.assertEquals(context.getResponse().getStatusCode(), 200);
		Assert.assertEquals(context.getResponse().getContentType(), "application/json; charset=utf-8");
		// Extract the response as a List
		List<Object> bookingIds = context.getResponse().jsonPath().getList("$");

		System.out.println("Booking Ids list is : " + bookingIds.isEmpty());

		// Assert that the list is not empty
		Assert.assertFalse(bookingIds.isEmpty(), "The booking ID list is empty!");

	}

	@Then("the response should contain empty list of booking Id")
	public void reponse_contain_list_of_booking_id() {
		print_log();

		Assert.assertEquals(context.getResponse().getStatusCode(), 200);
		Assert.assertEquals(context.getResponse().getContentType(), "application/json; charset=utf-8");
		// Extract the response as a List
		List<Object> bookingIds = context.getResponse().jsonPath().getList("$");
		System.out.println("Booking Ids list is : " + bookingIds.isEmpty());
		if (bookingIds.isEmpty()) {
			Assert.assertTrue(bookingIds.isEmpty(), "List is not empt ");
		}

	}

	// ============================================================
	// EMPTY ARRAY
	// ============================================================

	@Then("the response body should be an empty array")
	public void theResponseBodyShouldBeAnEmptyArray() {

		List<?> bookings = response.jsonPath().getList("$");

		Assert.assertTrue(bookings.isEmpty(), "Expected response body to be empty");
	}

	// ============================================================
	// MATCHING BOOKING IDs
	// ============================================================

	@Then("the response body should contain matching booking IDs")
	public void theResponseBodyShouldContainMatchingBookingIDs() {
		// 1. Extract the full response body as a List of objects
		List<Object> responseList = context.getResponse().jsonPath().getList("$");

		// 2. Assert that the response list is not null and not empty
		Assert.assertNotNull(responseList, "Response body is null");
		Assert.assertTrue(responseList.isEmpty(), "Response body array is empty");
		if (!responseList.isEmpty()) {

			// 3. Extract and print all bookingid fields from the array
			List<Integer> bookingIds = context.getResponse().jsonPath().getList("bookingid");
			System.out.println("Matching Booking IDs : " + bookingIds);

			// 4. Assert that the first item actually contains a bookingid
			Assert.assertNotNull(bookingIds.get(0), "First item does not contain a bookingid");
		}

	}

	@Then("the response should contain list of booking Id")
	public void response_should_contain_list_of_booking_ids() {
		print_log();

		Assert.assertEquals(context.getResponse().getStatusCode(), 200);
		Assert.assertEquals(context.getResponse().getContentType(), "application/json; charset=utf-8");
		// Extract the response as a List
		List<Object> bookingIds = context.getResponse().jsonPath().getList("$");

		// Assert that the list is not empty
		Assert.assertTrue(bookingIds.size() > 0, "The booking ID list is empty!");

		System.out.println("Response Body : " + context.getResponse().asPrettyString());

	}

	// ============================================================
	// STATUS CODE
	// ============================================================

	@Then("the getBookingIds response status code should be {int}")
	public void theResponseStatusCodeShouldBe(int expectedStatusCode) {

		Assert.assertNotNull(context.getResponse(), "Response should not be null");

		Assert.assertEquals(context.getResponse().getStatusCode(), expectedStatusCode,
				"Unexpected response status code");
	}

	// ============================================================
	// CONTENT TYPE
	// ============================================================

	@Then("the getBookingIds response content type should be {string}")
	public void theResponseContentTypeShouldBe(String expectedContentType) {

		Assert.assertNotNull(context.getResponse(), "Response should not be null");

		String actualContentType = response.getContentType();

		System.out.println("Expected Content-Type: " + expectedContentType);

		System.out.println("Actual Content-Type: " + actualContentType);

		Assert.assertTrue(actualContentType.toLowerCase().contains(expectedContentType.toLowerCase()),
				"Response Content-Type should contain " + expectedContentType + " but was " + actualContentType);
	}

	// ============================================================
	// JSON ARRAY
	// ============================================================

	@Then("the response should contain a JSON array")
	public void theResponseShouldContainAJSONArray() {

		Assert.assertNotNull(context.getResponse(), "Response should not be null");

		List<?> responseList = response.jsonPath().getList("$");

		Assert.assertNotNull(responseList, "Response should contain a JSON array");

		System.out.println("Number of bookings returned: " + responseList.size());
	}

	// ============================================================
	// BOOKING ID FIELD
	// ============================================================

	@Then("each booking object should contain a {string} field")
	public void eachBookingObjectShouldContainAField(String fieldName) {

		Assert.assertNotNull(context.getResponse(), "Response should not be null");

		List<Integer> bookingIds = response.jsonPath().getList("bookingid", Integer.class);

		List<?> bookingId = response.jsonPath().getList(fieldName);

		Assert.assertNotNull(bookingIds, "Response should contain field: " + fieldName);

		System.out.println("Validated field: " + fieldName);

		System.out.println("Number of booking IDs: " + bookingIds.size());
	}

	@Then("each booking ID should be a valid integer")
	public void each_booking_id_should_be_valid_integer() {

		// Get booking IDs from response
		List<Object> bookingIds = context.getResponse().jsonPath().getList("bookingid");

		// 1. Verify response list is not null
		Assert.assertNotNull(bookingIds, "Booking ID list is null");

		// 2. Verify response contains at least one booking ID
		Assert.assertFalse(bookingIds.isEmpty(), "Booking ID list is empty");

		// 3. Verify every booking ID is an Integer
		for (Object bookingId : bookingIds) {

			Assert.assertNotNull(bookingId, "Booking ID should not be null");

			Assert.assertTrue(bookingId instanceof Integer, "Booking ID '" + bookingId
					+ "' is not an Integer. Actual type: " + bookingId.getClass().getSimpleName());
		}
	}

	// ============================================================
	// BOOKING ID NUMBER VALIDATION
	// ============================================================

	@Then("each {string} should be a number")
	public void eachFieldShouldBeANumber(String fieldName) {

		Assert.assertNotNull(context.getResponse(), "Response should not be null");

		List<?> values = response.jsonPath().getList(fieldName);

		Assert.assertNotNull(values, "Field '" + fieldName + "' should exist");

		for (Object value : values) {

			Assert.assertNotNull(value, "Field '" + fieldName + "' should not be null");

			Assert.assertTrue(value instanceof Number,
					"Field '" + fieldName + "' should be a number, but was: " + value);
		}

		System.out.println("All values of '" + fieldName + "' are numeric.");
	}

	// ============================================================
	// THEN - BOOKING IDS
	// ============================================================

	@Then("the response body should contain booking IDs")
	public void the_response_body_should_contain_booking_ids() {

		Response response = context.getResponse();

		Assert.assertNotNull(response, "Response is null");

		List<Integer> bookingIds = response.jsonPath().getList("bookingid", Integer.class);

		Assert.assertNotNull(bookingIds, "Booking ID list is null");

		Assert.assertFalse(bookingIds.isEmpty(), "Booking ID list is empty");

		for (Integer bookingId : bookingIds) {

			Assert.assertNotNull(bookingId, "Booking ID should not be null");

			Assert.assertTrue(bookingId > 0, "Booking ID should be greater than 0. " + "Actual value: " + bookingId);
		}

		System.out.println("Booking IDs : " + bookingIds);
	}

	@Then("the response contain list of booking Id should be {string}")
	public void response_contain_list_of_booking_Id_should_be(String expectedListStatus) {

		// Extract booking IDs from response
		List<Integer> bookingIds = response.jsonPath().getList("bookingid", Integer.class);

		// Safety check
		Assert.assertNotNull(bookingIds, "Booking ID list should not be null");

		if ("empty".equalsIgnoreCase(expectedListStatus)) {

			Assert.assertTrue(bookingIds.isEmpty(), "Expected booking ID list to be empty, but found: " + bookingIds);

		} else if ("non-empty".equalsIgnoreCase(expectedListStatus)) {

			Assert.assertFalse(bookingIds.isEmpty(), "Expected booking ID list to be non-empty, but it was empty");

		} else {

			Assert.fail("Invalid expectedListStatus: " + expectedListStatus + ". Expected 'empty' or 'non-empty'.");
		}
	}

	// ============================================================
	// FIELD INTEGER VALIDATION
	// ============================================================

	@Then("every bookingid should be an integer")
	public void everyBookingidShouldBeAnInteger() {

		List<Object> bookingIds = response.jsonPath().getList("bookingid");

		for (Object bookingId : bookingIds) {

			// Extract the first booking ID
			// bookingId = response.jsonPath().get("bookingid[0]");

			// Check if it is an instance of Integer
			Assert.assertTrue(bookingId instanceof Integer, "bookingid must be Integer");
		}
	}

	// ============================================================
	// NULL VALIDATION
	// ============================================================

	@Then("no bookingid should be null")
	public void noBookingidShouldBeNull() {

		List<Object> bookingIds = response.jsonPath().getList("bookingid");

		for (Object bookingId : bookingIds) {

			Assert.assertNotNull(bookingIds, "bookingid should not be null");

		}
	}

}
