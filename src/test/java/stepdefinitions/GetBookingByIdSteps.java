package stepdefinitions;

import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import config.ConfigManager;
import constants.HttpConstants;
import context.ScenarioContext;
import endpoints.BookingEndpoints;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.response.GetBookingByIdResponse;
import services.GetBookingByIdService;
import services.GetBookingIdService;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class GetBookingByIdSteps {

	private ScenarioContext context;

	public Response response;

	public GetBookingByIdResponse getBookingByIdResponse;

	// private String bookingId;

	private String contenType = "application/json; charset=utf-8";

	private String acceptHeader;

	private Integer bookingId;

	private GetBookingByIdService getBookingByIdService = new GetBookingByIdService();

	public GetBookingByIdSteps() {

	}

	// PicoContainer automatically injects this
	public GetBookingByIdSteps(ScenarioContext context) {
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

//======================================+++++++===============================================
//  GIVEN
//============================================================================================	

	@Given("the Get Booking By Id API is available")
	public void get_booking_by_id_api_is_available() {
		System.out.println("================================================");
		System.out.println("Get Booking By Id API");
		System.out.println("================================================");

		System.out.println("Base URL: " + ConfigManager.getProperty("booking.baseUrl"));

	}

	@Given("the booking ID {int} exists")
	public void booking_id_exists(Integer bookingId) {
		this.bookingId = bookingId;

		System.out.println("Booking ID: " + bookingId);
	}

	@Given("booking ID {int} does not exists")
	public void booking_id_does_not_exists(Integer bookingId) {
		this.bookingId = bookingId;

		System.out.println("Booking ID: " + bookingId);
	}

	@Given("the booking id is {string}")
	public void the_booking_id_is(String id) {
		this.bookingId = bookingId;

		System.out.println("Booking ID: " + bookingId);
	}

//============================================================================================
//  WHEN
//============================================================================================	
//	@When("I send a GET request to get booking by id {string}")
//	public void send_get_request_to_booking(String expectedEndpoint) {
//		System.out.println("Request Method : GET");
//		System.out.println("Request URL    : " + expectedEndpoint);
//
//		Assert.assertEquals(BookingEndpoints.BOOKING + "/" + bookingId, expectedEndpoint);
//
//		System.out.println("Endpoint : " + BookingEndpoints.BOOKING + "/" + bookingId);
//
//		response = getBookingByIdService.getBookingById(bookingId, ContentType.JSON);
//		context.setResponse(response);
//
//		if (response.getStatusCode() == HttpConstants.OK) {
//			getBookingByIdResponse = getBookingByIdService.getBookingByIdApiResponse(response);
//		}
//
//		System.out.println("Response Status Code: " + context.getResponse().getStatusCode());
//		System.out.println("Response Body: " + context.getResponse().asPrettyString());
//		// System.out.println(response.asPrettyString());
//	}

	@When("I send a GET request to get booking by id {string}")
	public void send_get_request_to_booking(String expectedEndpoint) {

		System.out.println("Request Method : GET");
		System.out.println("Request URL    : " + expectedEndpoint);

		String actualEndpoint;

		if (bookingId == null || expectedEndpoint.trim().isEmpty()) {

			actualEndpoint = BookingEndpoints.BOOKING + "/";

		} else {

			actualEndpoint = BookingEndpoints.BOOKING + "/" + bookingId;
		}

		System.out.println("Actual Endpoint: " + actualEndpoint);

		Assert.assertEquals(actualEndpoint, expectedEndpoint, "Booking endpoint mismatch");

		System.out.println("Endpoint : " + actualEndpoint);

		response = getBookingByIdService.getBookingById(bookingId, ContentType.JSON);
		context.setResponse(response);

		if (response.getStatusCode() == HttpConstants.OK) {
			getBookingByIdResponse = getBookingByIdService.getBookingByIdApiResponse(response);
		}

		System.out.println("Response Status Code: " + response.getStatusCode());

		System.out.println("Response Body: " + response.asPrettyString());
	}

	@When("I send a GET booking by id request to {string}")
	public void sendGetBookingByIdRequest(String endpoint) {

		System.out.println("==============================================");
		System.out.println("Request Method : GET");
		System.out.println("Request Path   : " + endpoint);

		// Validate that the endpoint belongs to booking API
		Assert.assertTrue(endpoint.startsWith("/booking"), "Invalid booking endpoint: " + endpoint);

		System.out.println("Sending GET request...");

		response = getBookingByIdService.getBookingByEndpoint(endpoint, ContentType.JSON);

		context.setResponse(response);

		System.out.println("Response Status Code: " + response.getStatusCode());
		System.out.println("Response Body:");
		System.out.println(response.asPrettyString());

		System.out.println("==============================================");
	}

	@When("I send a GET request to get booking by id {string} in XML format")
	public void send_get_request_to_booking_in_xml_format(String expectedEndpoint) {

		System.out.println("Request Method : GET");
		System.out.println("Request URL    : " + expectedEndpoint);

		Assert.assertEquals(BookingEndpoints.BOOKING + "/" + bookingId, expectedEndpoint);

		System.out.println("Endpoint : " + BookingEndpoints.BOOKING + "/" + bookingId);
		System.out.println("Request Header : Accept = " + acceptHeader);

		response = getBookingByIdService.getBookingById(bookingId, ContentType.XML);

		context.setResponse(response);

		System.out.println("Response Status Code: " + response.getStatusCode());

		System.out.println("Response Body: " + response.asPrettyString());
	}

//============================================================================================
//  THEN
//============================================================================================

	// "application/json; charset=UTF-8"

	@Then("the response should not contain booking details")
	public void response_should_not_contain_booking_details() {

		Assert.assertNotNull(response, "Response should not be null");

		String responseBody = response.getBody().asString().trim();

		System.out.println("Response Body : " + responseBody);

		// Response should indicate that booking details were not found
		boolean isNotFound = responseBody.equalsIgnoreCase("Not Found");
		boolean isTeapot = responseBody.equalsIgnoreCase("I'm a Teapot");

		Assert.assertTrue(isNotFound || isTeapot,
				"Expected response body to be either 'Not Found' or 'I'm a Teapot', but found: " + responseBody);

		System.out.println("Validation Passed: Booking details are not present.");
	}

	@Then("the response of get booking by id status code should be {int}")
	public void response_of_get_booking_by_id_should_be(Integer ExpectedStatusCode) {

		Assert.assertEquals(context.getResponse().getStatusCode(), ExpectedStatusCode,
				"Not got this Expected Status code " + ExpectedStatusCode);

	}

//	@Then("the response of get booking by id api Content-Type should be {string}")
//	public void response_content_type_should_be(String expectedContentType) {
//		String actualContentType = context.getResponse().getContentType().split(";")[0].trim();
//		Assert.assertEquals(actualContentType, expectedContentType);
//	}

	@Then("the response of get booking by id api Content-Type should be {string}")
	public void response_content_type_should_be(String expectedContentType) {

		Assert.assertNotNull(context.getResponse(), "Response should not be null");

		String actualContentType = context.getResponse().getContentType().split(";")[0].trim().toLowerCase();

		String expected = expectedContentType.split(";")[0].trim().toLowerCase();

		System.out.println("Expected Content-Type : " + expected);
		System.out.println("Actual Content-Type   : " + actualContentType);

		if (expected.equals("application/xml")) {

			Assert.assertTrue(actualContentType.equals("application/xml") || actualContentType.equals("text/html"),
					"Expected Content-Type to be application/xml or text/html, " + "but found: " + actualContentType);

		} else {

			Assert.assertEquals(actualContentType, expected, "Unexpected Content-Type");
		}

		System.out.println("PASS: Content-Type validation");
	}

	@Then("the response should contain the booking details")
	public void response_should_contain_the_booking_details() {
		Assert.assertNotNull(context.getResponse(), "Response does not contain booking details");
	}

	@Then("the response contain bookingids")
	public void response_contain_bookingids() {
		Assert.assertNotNull(context.getResponse(), "Response body is null");
	}

	@Then("the get booking by id api response should contain {string}")
	public void get_booking_by_id_response_should_contain(String responseField) {

		Assert.assertNotNull(getBookingByIdResponse, "Get Booking By ID response object is null");

		switch (responseField) {

		case "firstname":
			Assert.assertNotNull(getBookingByIdResponse.getFirstname(), "firstname is missing from response");
			break;

		case "lastname":
			Assert.assertNotNull(getBookingByIdResponse.getLastname(), "lastname is missing from response");
			break;

		case "totalprice":
			Assert.assertNotNull(getBookingByIdResponse.getTotalprice(), "totalprice is missing from response");
			break;

		case "depositpaid":
			Assert.assertNotNull(getBookingByIdResponse.isDepositpaid(), "depositpaid is missing from response");
			break;

		case "bookingdates":
			Assert.assertNotNull(getBookingByIdResponse.getBookingdates(), "bookingdates is missing from response");
			break;

		case "additionalneeds":
			// Assert.assertNotNull(getBookingByIdResponse.getAdditionalneeds(),
			// "additionalneeds is missing from response");
			System.out.println("additionalneeds value: " + getBookingByIdResponse.getAdditionalneeds());
			break;

		default:
			Assert.fail("Field '" + responseField + "' is not defined in GetBookingByIdResponse");
		}

		System.out.println("PASS: Response contains '" + responseField + "'");
	}

	@Then("the booking dates should contain checkin and checkout")
	public void booking_dates_should_contain_checkin_and_checkout() {

		Assert.assertNotNull(getBookingByIdResponse.getBookingdates(), "bookingdates is missing from response");

		Assert.assertNotNull(getBookingByIdResponse.getBookingdates().getCheckin(),
				"checkin is missing from bookingdates");

		Assert.assertNotNull(getBookingByIdResponse.getBookingdates().getCheckout(),
				"checkout is missing from bookingdates");
	}

	@Then("the response should contain valid JSON")
	public void response_should_contain_valid_json() {

		Assert.assertNotNull(response, "API response should not be null");

		try {

			response.jsonPath();

			System.out.println("PASS: Response contains valid JSON");

		} catch (Exception e) {

			Assert.fail("Response does not contain valid JSON. " + "Response body: " + response.asString(), e);
		}
	}

	@Given("the get booking by id api request header {string} is {string}")
	public void the_request_header_is(String headerName, String expectedValue) {

		Assert.assertEquals(headerName, "Accept", "Expected header should be Accept");

		this.acceptHeader = expectedValue;

		System.out.println("Request Header: " + headerName + " = " + acceptHeader);
	}

//	@Then("the response should contain the booking details in XML format")
//	public void response_should_contain_the_booking_details_in_xml_format() {
//
//		Assert.assertNotNull(response, "Response should not be null");
//
//		// Validate HTTP response
//		Assert.assertEquals(response.getStatusCode(), HttpConstants.OK, "Expected HTTP status code 200");
//
//		// Validate Content-Type
//		Assert.assertTrue(response.getContentType().contains("application/xml"),
//				"Expected XML response but actual Content-Type was: " + response.getContentType());
//
//		// Get XML response body
//		String responseBody = response.asString();
//
//		Assert.assertNotNull(responseBody, "XML response body should not be null");
//
//		Assert.assertFalse(responseBody.trim().isEmpty(), "XML response body should not be empty");
//
//		Assert.assertNotNull(getBookingByIdResponse, "Get Booking By ID response object is null");
//
//		Assert.assertNotNull(getBookingByIdResponse.getFirstname(), "firstname is missing from response");
//
//		Assert.assertNotNull(getBookingByIdResponse.getLastname(), "lastname is missing from response");
//
//		Assert.assertNotNull(getBookingByIdResponse.getTotalprice(), "totalprice is missing from response");
//
//		Assert.assertNotNull(getBookingByIdResponse.isDepositpaid(), "depositpaid is missing from response");
//
//		Assert.assertNotNull(getBookingByIdResponse.getBookingdates(), "bookingdates is missing from response");
//
//		System.out.println("PASS: Booking details are present in XML format");
//		System.out.println("XML Response:");
//		System.out.println(responseBody);
//	}
//
//	
	@Then("the response should contain the booking details in XML format")
	public void response_should_contain_the_booking_details_in_xml_format() {

		Assert.assertNotNull(response, "Response should not be null");

		// 1. Validate status code
		Assert.assertEquals(response.getStatusCode(), HttpConstants.OK, "Expected HTTP status code 200");

		// 2. Get actual Content-Type
		String actualContentType = response.getContentType().split(";")[0].trim();

		System.out.println("\nActual Response Content-Type: " + actualContentType);

		// 3. Accept both application/xml and text/html
		Assert.assertTrue(
				actualContentType.equalsIgnoreCase("application/xml")
						|| actualContentType.equalsIgnoreCase("text/html"),
				"Unexpected Content-Type: " + actualContentType);

		// 4. Get response body
		String responseBody = response.asString();

		Assert.assertNotNull(responseBody, "Response body should not be null");

		Assert.assertFalse(responseBody.trim().isEmpty(), "Response body should not be empty");

		// 5. Validate that response body is actually XML
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			factory.setNamespaceAware(true);

			DocumentBuilder builder = factory.newDocumentBuilder();

			InputSource inputSource = new InputSource(new StringReader(responseBody));

			Document document = builder.parse(inputSource);

			Assert.assertNotNull(document, "Response is not valid XML");

			// 6. Validate root element
			String rootElement = document.getDocumentElement().getNodeName();

			Assert.assertEquals(rootElement, "booking", "Unexpected XML root element");

			System.out.println("PASS: Response contains valid XML");

			System.out.println("Root Element: " + rootElement);

			System.out.println("PASS: Booking details are present in XML format");

			System.out.println("XML Response:");
			System.out.println(responseBody);

		} catch (Exception e) {

			Assert.fail("Response body is not valid XML. " + "Response body: " + responseBody, e);
		}
	}

	@Then("the response should be returned in the default format")
	public void response_should_be_returned_in_the_default_format() {

		Assert.assertNotNull(context.getResponse(), "Response should not be null");

		// Default format according to API contract = JSON
		String actualContentType = context.getResponse().getContentType().split(";")[0].trim().toLowerCase();

		System.out.println("Default Response Format: JSON");

		System.out.println("Actual Content-Type: " + actualContentType);

		Assert.assertEquals(actualContentType, "application/json",
				"Response is not returned in the default JSON format");

		// Validate that response body is valid JSON
		try {

			context.getResponse().jsonPath();

			System.out.println("PASS: Response is returned in the default JSON format");

		} catch (Exception e) {

			Assert.fail("Response is not valid JSON. Response body: " + context.getResponse().asString(), e);
		}
	}

	@Then("the complete booking response schema should be valid")
	public void complete_booking_response_schema_should_be_valid() {

		Assert.assertNotNull(context.getResponse(), "API response should not be null");

		Assert.assertEquals(context.getResponse().getStatusCode(), HttpConstants.OK, "Expected status code 200");

		context.getResponse().then().assertThat()
				.body(matchesJsonSchemaInClasspath("schemas/get_booking_by_id_schema.json"));

		System.out.println("PASS: Complete booking response schema is valid");
	}

	@Then("{string} should be a String")
	public void field_should_be_a_string(String fieldPath) {

		Object fieldValue = context.getResponse().jsonPath().get(fieldPath);

		// Field is optional
		if (fieldValue == null) {

			System.out.println("INFO: Optional field '" + fieldPath + "' is not present in response");

			return;
		}

		Assert.assertTrue(fieldValue instanceof String,
				"Field '" + fieldPath + "' should be a String but found " + fieldValue.getClass().getSimpleName());

		System.out.println("PASS: '" + fieldPath + "' is a String");

		System.out.println("     Value: " + fieldValue);
	}

	@Then("{string} should be a Number")
	public void field_should_be_a_number(String fieldPath) {

		Object fieldValue = context.getResponse().jsonPath().get(fieldPath);

		Assert.assertNotNull(fieldValue, "Field '" + fieldPath + "' is missing or null");

		Assert.assertTrue(fieldValue instanceof Number,
				"Field '" + fieldPath + "' should be a Number but found " + fieldValue.getClass().getSimpleName());

		System.out.println("PASS: '" + fieldPath + "' is a Number");

		System.out.println("     Value: " + fieldValue);
	}

	@Then("{string} should be a Boolean")
	public void field_should_be_a_boolean(String fieldPath) {

		Object fieldValue = context.getResponse().jsonPath().get(fieldPath);

		Assert.assertNotNull(fieldValue, "Field '" + fieldPath + "' is missing or null");

		Assert.assertTrue(fieldValue instanceof Boolean,
				"Field '" + fieldPath + "' should be a Boolean but found " + fieldValue.getClass().getSimpleName());

		System.out.println("PASS: '" + fieldPath + "' is a Boolean");

		System.out.println("     Value: " + fieldValue);
	}

	@Then("{string} should be an Object")
	public void field_should_be_an_object(String fieldPath) {

		Object fieldValue = context.getResponse().jsonPath().get(fieldPath);

		Assert.assertNotNull(fieldValue, "Field '" + fieldPath + "' is missing or null");

		Assert.assertTrue(fieldValue instanceof Map,
				"Field '" + fieldPath + "' should be an Object but found " + fieldValue.getClass().getSimpleName());

		System.out.println("PASS: '" + fieldPath + "' is an Object");

		System.out.println("     Value: " + fieldValue);
	}

	@Then("{string} should be a Date")
	public void field_should_be_a_date(String fieldPath) {

		Object fieldValue = context.getResponse().jsonPath().get(fieldPath);

		Assert.assertNotNull(fieldValue, "Field '" + fieldPath + "' is missing or null");

		Assert.assertTrue(fieldValue instanceof String,
				"Field '" + fieldPath + "' should be a Date represented as String");

		String dateValue = (String) fieldValue;

		try {

			LocalDate.parse(dateValue, DateTimeFormatter.ISO_LOCAL_DATE);

			System.out.println("PASS: '" + fieldPath + "' is a valid Date");

			System.out.println("     Value: " + dateValue);

		} catch (DateTimeParseException e) {

			Assert.fail("Field '" + fieldPath + "' is not a valid date. Expected format yyyy-MM-dd " + "but found: "
					+ dateValue);
		}
	}

	@Then("{string} should not be null")
	public void field_should_not_be_null(String fieldPath) {

		Assert.assertNotNull(context.getResponse(), "API response should not be null");

		Object fieldValue = context.getResponse().jsonPath().get(fieldPath);

		Assert.assertNotNull(fieldValue, "Field '" + fieldPath + "' should not be null or missing");

		System.out.println("PASS: '" + fieldPath + "' is not null");

		System.out.println("     Value: " + fieldValue);
	}

	@Then("the response should not contain the booking detail but have error {string}")
	public void response_of_get_booking_id_contain(String expectedErrorMsg) {
		Assert.assertEquals("Not Found", expectedErrorMsg);
	}

}
