package stepdefinitions;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.ConfigManager;
import context.ScenarioContext;
import endpoints.BookingEndpoints;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.requests.Booking;
import models.requests.BookingDates;
import models.requests.BookingLocalDates;
import models.response.BookingDatesResponse;
import models.response.CreateBookingResponse;
import models.response.GetBookingByIdResponse;
import services.CreateBookingService;
import services.GetBookingByIdService;

public class CreateBookingSteps {

	private ScenarioContext context;

	public Response response;

	public GetBookingByIdResponse getBookingByIdResponse;

	// private String bookingId;

	// private String contenType = "application/json; charset=utf-8";

	private String contentType;
	private String acceptHeader;

	private Integer bookingId;
	private Booking bookingRequest;

	private CreateBookingService createBookingService;
	private CreateBookingResponse createBookingResponse;
	private BookingDatesResponse bookingDatesResponse;
	
	private BookingLocalDates bookingDates;

	public CreateBookingSteps() {

	}

	public CreateBookingSteps(ScenarioContext context) {
		this.context = context;
		this.bookingRequest = new Booking();
		this.createBookingService = new CreateBookingService();
		this.createBookingResponse = new CreateBookingResponse();
		//this.bookingDates = new BookingDatesResponse();
		this.bookingDates = new BookingLocalDates();
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

// =======================================================================================
// BACKGROUND
// =======================================================================================

	@Given("the create booking API endpoint is {string}")
	public void theRestfulBookingAPIIsAvailable(String expectedEndpoint) {

		System.out.println("================================================");
		System.out.println("Restful Booker API");
		System.out.println("Base URL: " + ConfigManager.getProperty("booking.baseUrl"));
		System.out.println("================================================");

		Assert.assertNotNull(ConfigManager.getProperty("booking.baseUrl"),
				"Restful Booker Base URL should not be null");
		Assert.assertEquals(BookingEndpoints.BOOKING, expectedEndpoint,
				"Create Booking Api " + expectedEndpoint + " is not endpoint");
	}

	// ============================================================
	// REQUEST CONTENT TYPE
	// ============================================================
	@Given("the create booking request Content-Type is {string}")
	public void create_booking_request_content_type_is(String expectedContentType) {
		// Convert Cucumber's literal string "null" into a true Java null
		if ("null".equalsIgnoreCase(expectedContentType) || expectedContentType == null) {
			this.contentType = null;
		} else {
			this.contentType = expectedContentType;
		}

		// Validate that the value is either null, JSON, or XML
		if (this.contentType != null) {
			boolean isValid = this.contentType.equals("application/json") || this.contentType.equals("application/xml");

			Assert.assertTrue(isValid,
					"Content-Type must be 'application/json' or 'application/xml', but found: " + this.contentType);
		}

		System.out.println("Request Content-Type: " + this.contentType);
	}

	@Given("in create booking request the Accept header is {string}")
	public void create_booking_req_accept_header_is(String expectedAcceptHeader) {
		// Convert Cucumber's literal string "null" into a true Java null
		if ("null".equalsIgnoreCase(expectedAcceptHeader) || expectedAcceptHeader == null) {
			this.acceptHeader = null;
		} else {
			this.acceptHeader = expectedAcceptHeader;
		}

		// Validate that the value is either null, JSON, or XML
		if (this.acceptHeader != null) {
			boolean isValid = this.acceptHeader.equals("application/json")
					|| this.acceptHeader.equals("application/xml");

			Assert.assertTrue(isValid,
					"Accept header must be 'application/json' or 'application/xml', but found: " + this.acceptHeader);
		}

		System.out.println("Accept header Content-Type: " + this.acceptHeader);
	}

	// ============================================================
	// CREATE VALID BOOKING REQUEST
	// ============================================================
	@Given("the create booking request contains valid booking details")
	public void theCreateBookingRequestContainsValidBookingDetails() {
		/*
		 * * BookingDates uses LocalDate. * * Therefore we must use: * *
		 * LocalDate.parse("2026-09-01") * * instead of passing String values.
		 */

		LocalDate checkin = LocalDate.parse("2026-09-01");
		LocalDate checkout = LocalDate.parse("2026-09-10");

		//BookingDatesResponse bookingDates = new BookingDatesResponse(checkin, checkout);
		
		BookingLocalDates bookingDates = new BookingLocalDates(checkin, checkout);

		// BookingDates bookingDates = new BookingDates( LocalDate.of(2026, 9, 1),
		// LocalDate.of(2026, 9, 10) );

		bookingRequest = new Booking("Jim", "Brown", 111, true, bookingDates, "Breakfast");

		// --------------------------------------------------------
		// Validate request object
		// --------------------------------------------------------
		Assert.assertNotNull(bookingRequest, "Booking request should not be null");
		Assert.assertEquals(bookingRequest.getFirstname(), "Jim", "Incorrect firstname");
		Assert.assertEquals(bookingRequest.getLastname(), "Brown", "Incorrect lastname");
		Assert.assertEquals(bookingRequest.getTotalprice(), 111, "Incorrect totalprice");
		Assert.assertTrue(bookingRequest.isDepositpaid(), "Depositpaid should be true");
		Assert.assertNotNull(bookingRequest.getBookingdates(), "Bookingdates should not be null");
		Assert.assertEquals(bookingRequest.getBookingdates().getCheckin(), LocalDate.of(2026, 9, 1),
				"Incorrect checkin date");
		Assert.assertEquals(bookingRequest.getBookingdates().getCheckout(), LocalDate.of(2026, 9, 10),
				"Incorrect checkout date");
		Assert.assertEquals(bookingRequest.getAdditionalneeds(), "Breakfast", "Incorrect additionalneeds");
		System.out.println("Valid booking request created successfully");
		System.out.println("Firstname : " + bookingRequest.getFirstname());
		System.out.println("Lastname : " + bookingRequest.getLastname());
		System.out.println("Total Price : " + bookingRequest.getTotalprice());
		System.out.println("Deposit Paid : " + bookingRequest.isDepositpaid());
		System.out.println("Check-in : " + bookingRequest.getBookingdates().getCheckin());
		System.out.println("Check-out : " + bookingRequest.getBookingdates().getCheckout());
		System.out.println("Additional Needs: " + bookingRequest.getAdditionalneeds());

	}

	@Given("the additionalneeds field is not provided")
	public void theAdditionalneedsFieldIsNotProvided() {
		Assert.assertNotNull(bookingRequest, "Booking request should be created before validating additionalneeds");
		/* * Explicitly set additionalneeds to null. */
		bookingRequest.setAdditionalneeds(null);
		System.out.println("additionalneeds field will not be provided in request");
	}

	@Given("the create booking request contains valid booking")
	public void create_booking_request_contains_valid_booking(DataTable dataTable) {

		Map<String, String> data = dataTable.asMaps(String.class, String.class).get(0);
		String firstname = data.get("firstname");
		String lastname = data.get("lastname");
		int totalprice = Integer.parseInt(data.get("totalprice"));
		boolean depositpaid = Boolean.parseBoolean(data.get("depositpaid"));

		LocalDate checkin = LocalDate.parse(data.get("checkin"));
		LocalDate checkout = LocalDate.parse(data.get("checkout"));

		BookingLocalDates bookingDates = new BookingLocalDates(checkin, checkout);

		String additionalneeds = data.get("additionalneeds");

		/* * additionalneeds is intentionally NOT provided. */
		bookingRequest = new Booking(firstname, lastname, totalprice, depositpaid, bookingDates, additionalneeds);

		// --------------------------------------------------------
		// Validate request object
		// --------------------------------------------------------
		Assert.assertNotNull(bookingRequest, "Booking request should not be null");
		Assert.assertEquals(bookingRequest.getFirstname(), "Priya", "Incorrect firstname");
		Assert.assertEquals(bookingRequest.getLastname(), "Patil", "Incorrect lastname");
		Assert.assertEquals(bookingRequest.getTotalprice(), 200, "Incorrect totalprice");
		Assert.assertTrue(bookingRequest.isDepositpaid(), "Depositpaid should be true");
		Assert.assertNotNull(bookingRequest.getBookingdates(), "Bookingdates should not be null");
		Assert.assertEquals(bookingRequest.getBookingdates().getCheckin(), LocalDate.of(2026, 8, 1),
				"Incorrect checkin date");
		Assert.assertEquals(bookingRequest.getBookingdates().getCheckout(), LocalDate.of(2026, 9, 1),
				"Incorrect checkout date");
		Assert.assertEquals(bookingRequest.getAdditionalneeds(), "Lunch", "Incorrect additionalneeds");
		System.out.println("Valid booking request created successfully");
		System.out.println("Firstname : " + bookingRequest.getFirstname());
		System.out.println("Lastname : " + bookingRequest.getLastname());
		System.out.println("Total Price : " + bookingRequest.getTotalprice());
		System.out.println("Deposit Paid : " + bookingRequest.isDepositpaid());
		System.out.println("Check-in : " + bookingRequest.getBookingdates().getCheckin());
		System.out.println("Check-out : " + bookingRequest.getBookingdates().getCheckout());
		System.out.println("Additional Needs: " + bookingRequest.getAdditionalneeds());

	}

	@Given("in create booking request the Accept header is not specified")
	public void create_booking_req_accept_header_is_not_specified() {
		this.acceptHeader = null;
	}

	@Given("the depositpaid value is {string}")
	public void depositpaid_value_is(String expectedDepositpaid) {
		boolean expectedDepositPaid = Boolean.parseBoolean(expectedDepositpaid);
		if (expectedDepositPaid)
			Assert.assertTrue(expectedDepositPaid, "The deposited paid value is false");
		else
			Assert.assertFalse(expectedDepositPaid, "The deposited paid value is True");
	}

	@Given("the additionalneeds is {string}")
	public void additionalneeds_is(String expectedAdditionalNeeds) {
		Assert.assertNotNull(expectedAdditionalNeeds, expectedAdditionalNeeds + "is null");
	}

	@Given("the create booking request contains a valid firstname {string}")
	public void request_contains_a_valid_firstname(String firstname) {
		bookingRequest.setFirstname(firstname);
	}

	@Given("the create booking request contains a valid lastname {string}")
	public void request_contains_a_valid_lastname(String lastname) {
		bookingRequest.setLastname(lastname);
	}

	@Given("the create booking request contains a valid totalprice {int}")
	public void request_contains_a_valid_totalprice(int totalPrice) {
		bookingRequest.setTotalprice(totalPrice);
	}

	@Given("the create booking request contains a valid depositpaid {string}")
	public void request_contains_a_valid_totalprice(String depositpaid) {
		boolean depositPaid = Boolean.parseBoolean(depositpaid);
		bookingRequest.setDepositpaid(depositPaid);
	}

	@Given("the create booking request contains a valid bookingdates have checkin {string} & checkout {string}")
	public void request_contains_a_valid_bookingdates(String checkIn, String checkOut) {

		LocalDate checkin = LocalDate.parse(checkIn);
		LocalDate checkout = LocalDate.parse(checkOut);

		BookingLocalDates bookingDates = new BookingLocalDates(checkin, checkout);

		bookingRequest.setBookingdates(bookingDates);
	}

	@Given("the create booking request contains a valid additionalneeds {string}")
	public void request_contains_a_valid_bookingdates(String additinalNeeds) {

		bookingRequest.setAdditionalneeds(additinalNeeds);
	}

	@Given("the request contains a valid {word} {string}")
	public void theRequestContainsAValidField(String fieldName, String value) {

		Assert.assertNotNull(bookingRequest, "Booking request should be initialized before setting " + fieldName);

		switch (fieldName.toLowerCase()) {

		case "firstname":
			bookingRequest.setFirstname(value);
			break;

		case "lastname":
			bookingRequest.setLastname(value);
			break;

		case "totalprice":
			bookingRequest.setTotalprice(Integer.parseInt(value));
			break;

		case "depositpaid":
			bookingRequest.setDepositpaid(Boolean.parseBoolean(value));
			break;

		case "additionalneeds":
			bookingRequest.setAdditionalneeds(value);
			break;

		default:
			Assert.fail("Unsupported booking field: " + fieldName);
		}

		System.out.println("Valid " + fieldName + ": " + value);
	}

	@Given("the request contains valid booking dates from {string} to {string}")
	public void theRequestContainsValidBookingDates(String checkin, String checkout) {

		LocalDate checkinDate = LocalDate.parse(checkin);
		LocalDate checkoutDate = LocalDate.parse(checkout);

		Assert.assertFalse(checkoutDate.isBefore(checkinDate), "Checkout date should not be before checkin date");

		BookingLocalDates bookingDates = new BookingLocalDates(checkinDate, checkoutDate);

		bookingRequest.setBookingdates(bookingDates);

		System.out.println("Check-in  : " + checkin);
		System.out.println("Check-out : " + checkout);
	}

	// ========================================================================================================
	// WHEN
	// ========================================================================================================

	// =============================================================
	// SEND POST REQUEST
	// =============================================================
	@When("the user sends a POST request to create booking")
	public void theUserSendsAPostRequestToCreateBooking() {
		Assert.assertNotNull(bookingRequest, "Booking request must be created before sending POST request");
		// Assert.assertNotNull( contentType, "Content-Type must be specified before
		// sending request" );
		/*
		 * * API contract supports: * \ * Content-Type : application/json * Accept :
		 * application/json
		 */
		// acceptHeader = "application/json";
		response = createBookingService.createBooking(contentType, acceptHeader, bookingRequest);
		context.setResponse(response);
		createBookingResponse = createBookingService.createBookingApiResponse(response);
		Assert.assertNotNull(response, "API response should not be null");
		System.out.println("\n========== CREATE BOOKING RESPONSE ==========");
		System.out.println(response.asPrettyString());
		System.out.println("=============================================\n");

	}

//	@When("the user sends a POST request to create booking with booking details")
//	public void theUserSendsAPostRequestToCreateBooking_() {
//
//		Assert.assertNotNull(bookingRequest, "Booking request must be created before sending POST request");
//
//		response = createBookingService.createBooking(contentType, acceptHeader, bookingRequest);
//
//		Assert.assertNotNull(response, "API response should not be null");
//
//		context.setResponse(response);
//
//		System.out.println("\n========== CREATE BOOKING REQUEST ==========");
//		System.out.println("Content-Type : " + contentType);
//		System.out.println("Accept       : " + acceptHeader);
//		System.out.println("Request Body :");
//		System.out.println(JsonPath.from(response.getBody().asString()));
//
//		System.out.println("\n========== CREATE BOOKING RESPONSE ==========");
//		System.out.println("Status Code  : " + response.getStatusCode());
//		System.out.println("Content-Type : " + response.getContentType());
//		System.out.println("Response Body:");
//		System.out.println(response.asPrettyString());
//		System.out.println("=============================================\n");
//	}

//		@When("the user sends a POST request to create booking with booking details")
//		public void theUserSendsAPostRequestToCreateBooking_() {
//	
//			Assert.assertNotNull(bookingRequest, "Booking request must be created before sending POST request");
//	
//			System.out.println("\n========== CREATE BOOKING REQUEST ==========");
//			System.out.println("Content-Type : " + contentType);
//			System.out.println("Accept       : " + acceptHeader);
//	
//			try {
//				ObjectMapper objectMapper = new ObjectMapper();
//				objectMapper.findAndRegisterModules();
//	
//				String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookingRequest);
//	
//				System.out.println("Request Body :");
//				System.out.println(requestBody);
//	
//			} catch (Exception e) {
//				throw new AssertionError("Unable to serialize booking request: " + e.getMessage(), e);
//			}
//	
//			response = createBookingService.createBooking(contentType, acceptHeader, bookingRequest);
//	
//			Assert.assertNotNull(response, "API response should not be null");
//	
//			context.setResponse(response);
//	
//			System.out.println("\n========== CREATE BOOKING RESPONSE ==========");
//			System.out.println("Status Code  : " + response.getStatusCode());
//			System.out.println("Content-Type : " + response.getContentType());
//			System.out.println("Response Body:");
//			System.out.println(response.asPrettyString());
//			System.out.println("=============================================\n");
//		}
	
	
	@When("the user sends a POST request to create booking with booking details")
	public void theUserSendsAPostRequestToCreateBookingWithBookingDetails() {

	    Assert.assertNotNull(
	            bookingRequest,
	            "Booking request must be created before sending POST request"
	    );

	    Assert.assertNotNull(
	            bookingRequest.getFirstname(),
	            "Firstname must not be null"
	    );

	    Assert.assertNotNull(
	            bookingRequest.getLastname(),
	            "Lastname must not be null"
	    );

	    Assert.assertNotNull(
	            bookingRequest.getBookingdates(),
	            "Booking dates must not be null"
	    );

	    System.out.println("\n========== CREATE BOOKING REQUEST ==========");
	    System.out.println("Content-Type : " + contentType);
	    System.out.println("Accept       : " + acceptHeader);

	    try {

	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.findAndRegisterModules();

	        String requestBody =
	                objectMapper
	                        .writerWithDefaultPrettyPrinter()
	                        .writeValueAsString(bookingRequest);

	        System.out.println("Request Body:");
	        System.out.println(requestBody);

	    } catch (Exception e) {

	        throw new AssertionError(
	                "Unable to serialize booking request: "
	                        + e.getMessage(),
	                e
	        );
	    }

	    response = createBookingService.createBooking(
	            contentType,
	            acceptHeader,
	            bookingRequest
	    );

	    Assert.assertNotNull(
	            response,
	            "API response should not be null"
	    );

	    context.setResponse(response);

	    System.out.println("\n========== CREATE BOOKING RESPONSE ==========");
	    System.out.println("Status Code  : " + response.getStatusCode());
	    System.out.println("Content-Type : " + response.getContentType());
	    System.out.println("Response Body:");
	    System.out.println(response.asPrettyString());
	    System.out.println("=============================================\n");
	}
	
	

//===============================================================================================================	
// THEN
//===============================================================================================================
	// =============================================================
	// STATUS CODE
	// =============================================================
	@Then("the create booking api response status code should be {int}")
	public void theCreateBookingApiResponseStatusCodeShouldBe(int expectedStatusCode) {
		Assert.assertNotNull(response, "Response should not be null");
		int actualStatusCode = response.getStatusCode();
		Assert.assertEquals(actualStatusCode, expectedStatusCode, "Unexpected response status code");
		System.out.println("Response Status Code: " + actualStatusCode);
	}

	// =============================================================
	// RESPONSE CONTENT TYPE
	// =============================================================
	@Then("the create booking api response Content-Type should be {string}")
	public void theCreateBookingApiResponseContentTypeShouldBe(String expectedContentType) {
		Assert.assertNotNull(response, "Response should not be null");
		String actualContentType = response.getContentType();
		Assert.assertTrue(actualContentType.contains(expectedContentType),
				"Expected Content-Type: " + expectedContentType + " but actual Content-Type: " + actualContentType);
		System.out.println("Response Content-Type: " + actualContentType);
	}

	// =============================================================
	// BOOKING ID
	// =============================================================
	@Then("the create booking api response should contain a numeric {string}")
	public void theCreateBookingApiResponseShouldContainANumericField(String fieldName) {
		Assert.assertNotNull(response, "Response should not be null");
		Integer bookingId = response.jsonPath().getInt(fieldName);
		Assert.assertNotNull(bookingId, fieldName + " should not be null");
		Assert.assertTrue(bookingId > 0, fieldName + " should be greater than zero");
		System.out.println("Booking ID: " + bookingId);

	}

	// =============================================================
	// BOOKING OBJECT
	// =============================================================
	@Then("the create booking api response should contain {string} object")
	public void theCreateBookingApiResponseShouldContainObject(String objectName) {
		Assert.assertNotNull(response, "Response should not be null");
		Object bookingObject = response.jsonPath().get(objectName);
		Assert.assertNotNull(bookingObject, objectName + " object should exist in response");
		System.out.println(objectName + " object exists in response");

	}

	// =============================================================
	// FIRSTNAME
	// =============================================================
	@Then("the create booking api booking firstname should match the request")
	public void theCreateBookingApiBookingFirstnameShouldMatchTheRequest() {
		String responseFirstname = response.jsonPath().getString("booking.firstname");
		Assert.assertEquals(responseFirstname, bookingRequest.getFirstname(),
				"Firstname in response does not match request");
		System.out.println("Firstname validated: " + responseFirstname);
	}

	// =============================================================
	// LASTNAME
	// =============================================================
	@Then("the create booking api booking lastname should match the request")
	public void theCreateBookingApiBookingLastnameShouldMatchTheRequest() {
		String responseLastname = response.jsonPath().getString("booking.lastname");
		Assert.assertEquals(responseLastname, bookingRequest.getLastname(),
				"Lastname in response does not match request");
		System.out.println("Lastname validated: " + responseLastname);
	}

	// =============================================================
	// TOTAL PRICE
	// =============================================================
	@Then("the create booking api booking totalprice should match the request")
	public void theCreateBookingApiBookingTotalpriceShouldMatchTheRequest() {
		int responseTotalPrice = response.jsonPath().getInt("booking.totalprice");
		Assert.assertEquals(responseTotalPrice, bookingRequest.getTotalprice(),
				"Totalprice in response does not match request");
		System.out.println("Total Price validated: " + responseTotalPrice);

	}

	// =============================================================
	// DEPOSIT PAID
	// =============================================================
	@Then("the create booking api booking depositpaid should match the request")
	public void theCreateBookingApiBookingDepositpaidShouldMatchTheRequest() {
		boolean responseDepositPaid = response.jsonPath().getBoolean("booking.depositpaid");
		Assert.assertEquals(responseDepositPaid, bookingRequest.isDepositpaid(),
				"Depositpaid in response does not match request");
		System.out.println("Deposit Paid validated: " + responseDepositPaid);

	}

	// =============================================================
	// CHECK-IN
	// =============================================================
	@Then("the create booking api booking checkin should match the request")
	public void theCreateBookingApiBookingCheckinShouldMatchTheRequest() {
		String responseCheckin = response.jsonPath().getString("booking.bookingdates.checkin");
		String requestCheckin = bookingRequest.getBookingdates().getCheckin().toString();
		Assert.assertEquals(responseCheckin, requestCheckin, "Check-in date in response does not match request");
		System.out.println("Check-in validated: " + responseCheckin);

	}

	// =============================================================
	// CHECK-OUT
	// =============================================================
	@Then("the create booking api booking checkout should match the request")
	public void theCreateBookingApiBookingCheckoutShouldMatchTheRequest() {
		String responseCheckout = response.jsonPath().getString("booking.bookingdates.checkout");
		String requestCheckout = bookingRequest.getBookingdates().getCheckout().toString();
		Assert.assertEquals(responseCheckout, requestCheckout, "Check-out date in response does not match request");
		System.out.println("Check-out validated: " + responseCheckout);

	}

	// =============================================================
	// ADDITIONAL NEEDS
	// =============================================================
	@Then("the create booking api booking additionalneeds should match the request")
	public void theCreateBookingApiBookingAdditionalneedsShouldMatchTheRequest() {
		String responseAdditionalNeeds = response.jsonPath().getString("booking.additionalneeds");
		Assert.assertEquals(responseAdditionalNeeds, bookingRequest.getAdditionalneeds(),
				"Additionalneeds in response does not match request");
		System.out.println("Additional Needs validated: " + responseAdditionalNeeds);

	}

	// =============================================================
	// RESPONSE JSON FORMAT
	// =============================================================

	@Then("the create booking api response should be returned in JSON format")
	public void theCreateBookingApiResponseShouldBeReturnedInJsonFormat() {

		// Validate response exists
		Assert.assertNotNull(response, "Create booking API response should not be null");

		// Validate Content-Type
		String actualContentType = response.getContentType();

		Assert.assertNotNull(actualContentType, "Response Content-Type should not be null");

		Assert.assertTrue(actualContentType.toLowerCase().contains("application/json"),
				"Expected response Content-Type to be JSON, but found: " + actualContentType);

		// Validate that response body is valid JSON
		try {

			response.jsonPath().getMap("$");

		} catch (Exception e) {

			Assert.fail("Create booking API response is not a valid JSON format. " + "Response body: "
					+ response.asString(), e);
		}

		System.out.println("Response returned in valid JSON format");

		System.out.println("Response Content-Type: " + actualContentType);
	}

	// =============================================================
	// RESPONSE XML FORMAT
	// =============================================================

	@Then("the create booking api response should be returned in XML format")
	public void create_booking_response_should_returned_in_xml() {

		Assert.assertNotNull(response, "Create booking API response should not be null");

		String actualContentType = response.getContentType();

		Assert.assertNotNull(actualContentType, "Response Content-Type should not be null");

		System.out.println("Actual Response Content-Type: " + actualContentType);

		// First validate Content-Type
		boolean isXmlContentType = actualContentType.toLowerCase().contains("application/xml")
				|| actualContentType.toLowerCase().contains("text/xml");

		Assert.assertTrue(isXmlContentType, "Expected XML response but received Content-Type: " + actualContentType
				+ "\nResponse body: " + response.asString());

		// Then validate XML syntax
		try {

			response.xmlPath().get();

			System.out.println("Response returned in valid XML format");

		} catch (Exception e) {

			Assert.fail("Response Content-Type indicates XML, " + "but response body is not valid XML."
					+ "\nResponse body: " + response.asString(), e);
		}
	}

	// @Then("the create booking api response should be returned in XML format")
	public void create_booking_response_should_returned_in_xml_() {

		// Validate response exists
		Assert.assertNotNull(response, "Create booking API response should not be null");

		// Get response Content-Type
		String actualContentType = response.getContentType();

		Assert.assertNotNull(actualContentType, "Response Content-Type should not be null");

		// Validate Content-Type is XML
		Assert.assertTrue(
				actualContentType.toLowerCase().contains("application/xml")
						|| actualContentType.toLowerCase().contains("text/xml")
						|| actualContentType.toLowerCase().contains("application/json"),
				"Expected response Content-Type to be XML, but found: " + actualContentType);

		// Validate response body is valid XML
		try {

			response.xmlPath().getString("//*");

		} catch (Exception e) {

			Assert.fail(
					"Create booking API response is not a valid XML format. " + "Response body: " + response.asString(),
					e);
		}

		System.out.println("Response returned in valid XML format");

		System.out.println("Response Content-Type: " + actualContentType);
	}

	@Then("the response of create booking api depositpaid should be {string}")
	public void response_of_create_booking_depositpaid_should_be(String expectedDepositpaid) {
		boolean expectedDepositPaid = Boolean.parseBoolean(expectedDepositpaid);

		boolean actualDepositPaid = response.jsonPath().getBoolean("booking.depositpaid");
		if (expectedDepositPaid)
			Assert.assertEquals(actualDepositPaid, expectedDepositPaid);

	}

	@Then("the response additionalneeds should be {string}")
	public void response_additionalNeeds_should_be(String expectedAdditionalNeeds) {
		String actualAdditionalNeeds = context.getResponse().jsonPath().getString("booking.additionalneeds");
		System.out.println("additionalNeeds : " + actualAdditionalNeeds);
		Assert.assertNotNull(expectedAdditionalNeeds, expectedAdditionalNeeds + " is null");
		Assert.assertEquals(actualAdditionalNeeds, expectedAdditionalNeeds);
	}

	@Then("the booking should be created successfully")
	public void theBookingShouldBeCreatedSuccessfully() {
		Assert.assertNotNull(response, "API response should not be null");
		// Validate bookingid
		Integer bookingId = response.jsonPath().getInt("bookingid");
		Assert.assertNotNull(bookingId, "bookingid should be present in response");
		Assert.assertTrue(bookingId > 0, "bookingid should be greater than zero");
		// Validate booking object
		Object bookingObject = response.jsonPath().get("booking");
		Assert.assertNotNull(bookingObject, "booking object should be present in response");
		// Validate request fields
		String firstname = response.jsonPath().getString("booking.firstname");
		String lastname = response.jsonPath().getString("booking.lastname");
		Integer totalprice = response.jsonPath().getInt("booking.totalprice");
		Boolean depositpaid = response.jsonPath().getBoolean("booking.depositpaid");
		Assert.assertEquals(firstname, bookingRequest.getFirstname(), "Firstname does not match");
		Assert.assertEquals(lastname, bookingRequest.getLastname(), "Lastname does not match");
		Assert.assertEquals(totalprice, bookingRequest.getTotalprice(), "Total price does not match");
		Assert.assertEquals(depositpaid, bookingRequest.isDepositpaid(), "Deposit paid does not match");
		System.out.println("Booking created successfully");
		System.out.println("Booking ID: " + bookingId);
	}

	@Then("the create booking response {word} should be {string}")
	public void validateCreateBookingResponseField(String fieldName, String expectedValue) {

		Response apiResponse = context.getResponse();

		Assert.assertNotNull(apiResponse, "Create booking API response should not be null");

		Map<String, String> fieldPaths = new HashMap<>();

		fieldPaths.put("firstname", "booking.firstname");
		fieldPaths.put("lastname", "booking.lastname");
		fieldPaths.put("totalprice", "booking.totalprice");
		fieldPaths.put("depositpaid", "booking.depositpaid");
		fieldPaths.put("additionalneeds", "booking.additionalneeds");
		fieldPaths.put("checkin", "booking.bookingdates.checkin");
		fieldPaths.put("checkout", "booking.bookingdates.checkout");

		String normalizedField = fieldName.toLowerCase().trim();

		Assert.assertTrue(fieldPaths.containsKey(normalizedField), "Unsupported response field: " + fieldName);

		String jsonPath = fieldPaths.get(normalizedField);

		Object actualValue = apiResponse.jsonPath().get(jsonPath);

		Assert.assertNotNull(actualValue, "Response field '" + fieldName + "' should not be null");

		String actualValueString = String.valueOf(actualValue);

		System.out.println("Field: " + fieldName + " | Expected: " + expectedValue + " | Actual: " + actualValueString);

		Assert.assertEquals(actualValueString, expectedValue,
				"Response field '" + fieldName + "' does not match expected value");
	}

	// @Then("the create booking response {word} should be {string}")
	public void theCreateBookingResponseFieldShouldBe_not_using(String fieldName, String expectedValue) {

		Assert.assertNotNull(response, "Create booking API response should not be null");

		String jsonPath = "booking." + fieldName;

		Object actualValue = response.jsonPath().get(jsonPath);

		Assert.assertNotNull(actualValue, "Response field '" + fieldName + "' should not be null");

		System.out.println("Response " + fieldName + ": " + actualValue);

		Assert.assertEquals(String.valueOf(actualValue), expectedValue,
				"Response field '" + fieldName + "' does not match expected value");

		System.out.println(fieldName + " validated successfully: " + actualValue);
	}

//	@Then("the create booking response {word} should be {string}")
//	public void validateCreateBookingResponseField(String fieldName, String expectedValue) {
//
//		Assert.assertNotNull(response, "Create booking API response should not be null");
//
//		/*
//		 * Map feature-file field names to JSON paths
//		 */
//		Map<String, String> fieldPaths = new HashMap<>();
//
//		fieldPaths.put("firstname", "booking.firstname");
//		fieldPaths.put("lastname", "booking.lastname");
//		fieldPaths.put("totalprice", "booking.totalprice");
//		fieldPaths.put("depositpaid", "booking.depositpaid");
//		fieldPaths.put("additionalneeds", "booking.additionalneeds");
//		fieldPaths.put("checkin", "booking.bookingdates.checkin");
//		fieldPaths.put("checkout", "booking.bookingdates.checkout");
//
//		String normalizedField = fieldName.toLowerCase().trim();
//
//		/*
//		 * Validate that the requested field is supported
//		 */
//		Assert.assertTrue(fieldPaths.containsKey(normalizedField), "Unsupported response field: " + fieldName);
//
//		String jsonPath = fieldPaths.get(normalizedField);
//
//		/*
//		 * Extract actual value dynamically
//		 */
//		Object actualValue = response.jsonPath().get(jsonPath);
//
//		Assert.assertNotNull(actualValue, "Response field '" + fieldName + "' should not be null");
//
//		/*
//		 * Convert actual value to String so that String, Integer, Boolean and date
//		 * values can all be compared using the same method.
//		 */
//		String actualValueString = String.valueOf(actualValue);
//
//		System.out.println("Response Field : " + fieldName);
//		System.out.println("JSON Path      : " + jsonPath);
//		System.out.println("Expected Value : " + expectedValue);
//		System.out.println("Actual Value   : " + actualValueString);
//
//		/*
//		 * Generic assertion
//		 */
//		Assert.assertEquals(actualValueString, expectedValue,
//				"Response field '" + fieldName + "' does not match expected value");
//
//		System.out.println("PASS: " + fieldName + " = " + actualValueString);
//	}

}
