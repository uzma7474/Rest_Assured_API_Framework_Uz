//package docs;
//
//import static org.testng.Assert.assertEquals;
//import static org.testng.Assert.assertFalse;
//import static org.testng.Assert.assertNotNull;
//import static org.testng.Assert.assertTrue;
//
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.restassured.response.Response;
//
//public class CreateEventSteps {
//
//	private final CreateEventService createEventsService = new CreateEventService();
//
//	private Response response;
//
//	private CreateEventRequest createEventRequest;
//
//	@Given("the user has a valid authentication token")
//	public void userHasValidAuthenticationToken() {
//
//		// TokenManager is expected to already contain
//		// a valid authentication token.
//
//		assertNotNull(utils.TokenManager.getToken(), "Authentication token should not be null");
//	}
//
//	@When("the user creates an event with valid details")
//	public void userCreatesEventWithValidDetails() {
//
//		createEventRequest = new CreateEventRequest("Tech Summit 2026",
//				"A premier technology conference bringing together industry leaders.", "Conference",
//				"Bangalore International Centre", "Bangalore", "2026-09-15T09:00:00.000Z", 1500, 500,
//				"https://example.com/images/tech-summit.jpg");
//
//		response = createEventsService.createEvent(createEventRequest);
//	}
//
//	@Then("the response status code should be {int}")
//	public void responseStatusCodeShouldBe(int expectedStatusCode) {
//
//		assertEquals(response.getStatusCode(), expectedStatusCode, "Unexpected response status code");
//	}
//
//	@Then("the response success should be true")
//	public void responseSuccessShouldBeTrue() {
//
//		assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
//	}
//
//	@Then("the response success should be false")
//	public void responseSuccessShouldBeFalse() {
//
//		assertFalse(response.jsonPath().getBoolean("success"), "Expected success=false");
//	}
//
//	@Then("the response message should be {string}")
//	public void responseMessageShouldBe(String expectedMessage) {
//
//		assertEquals(response.jsonPath().getString("message"), expectedMessage);
//	}
//
//	@Then("the event id should not be null")
//	public void eventIdShouldNotBeNull() {
//
//		Integer eventId = response.jsonPath().getInt("data.id");
//
//		assertNotNull(eventId, "Event ID should not be null");
//	}
//
//	@Then("available seats should equal total seats")
//	public void availableSeatsShouldEqualTotalSeats() {
//
//		int totalSeats = response.jsonPath().getInt("data.totalSeats");
//
//		int availableSeats = response.jsonPath().getInt("data.availableSeats");
//
//		assertEquals(availableSeats, totalSeats, "availableSeats should equal totalSeats");
//	}
//
//	@Then("the response title should match the request title")
//	public void responseTitleShouldMatchRequestTitle() {
//
//		assertEquals(response.jsonPath().getString("data.title"), createEventRequest.getTitle());
//	}
//
//	@Then("the response category should match the request category")
//	public void responseCategoryShouldMatchRequestCategory() {
//
//		assertEquals(response.jsonPath().getString("data.category"), createEventRequest.getCategory());
//	}
//
//	@Then("the response venue should match the request venue")
//	public void responseVenueShouldMatchRequestVenue() {
//
//		assertEquals(response.jsonPath().getString("data.venue"), createEventRequest.getVenue());
//	}
//
//	@Then("the response city should match the request city")
//	public void responseCityShouldMatchRequestCity() {
//
//		assertEquals(response.jsonPath().getString("data.city"), createEventRequest.getCity());
//	}
//
//	@Then("the response price should match the request price")
//	public void responsePriceShouldMatchRequestPrice() {
//
//		double responsePrice = response.jsonPath().getDouble("data.price");
//
//		assertEquals(responsePrice, createEventRequest.getPrice(), 0.001);
//	}
//
//	@Then("the response total seats should match the request total seats")
//	public void responseTotalSeatsShouldMatchRequestTotalSeats() {
//
//		int responseTotalSeats = response.jsonPath().getInt("data.totalSeats");
//
//		assertEquals(responseTotalSeats, createEventRequest.getTotalSeats());
//	}
//
//	@Then("the response eventDate should match the request eventDate")
//	public void responseEventDateShouldMatchRequestEventDate() {
//
//		assertEquals(response.jsonPath().getString("data.eventDate"), createEventRequest.getEventDate());
//	}
//
//	@Then("the response should contain createdAt")
//	public void responseShouldContainCreatedAt() {
//
//		assertNotNull(response.jsonPath().getString("data.createdAt"), "createdAt should not be null");
//	}
//
//	@Then("the response should contain updatedAt")
//	public void responseShouldContainUpdatedAt() {
//
//		assertNotNull(response.jsonPath().getString("data.updatedAt"), "updatedAt should not be null");
//	}
//
//	// ---------------------------------------------------------
//	// Negative scenarios
//	// ---------------------------------------------------------
//
//	@When("the user creates an event without authorization")
//	public void userCreatesEventWithoutAuthorization() {
//
//		String requestBody = """
//				{
//				  "title": "Tech Summit 2026",
//				  "description": "Technology conference",
//				  "category": "Conference",
//				  "venue": "Bangalore International Centre",
//				  "city": "Bangalore",
//				  "eventDate": "2026-09-15T09:00:00.000Z",
//				  "price": 1500,
//				  "totalSeats": 500,
//				  "imageUrl": "https://example.com/images/event.jpg"
//				}
//				""";
//
//		response = createEventsService.createEventWithoutAuthorization(requestBody);
//	}
//
//	@When("the user creates an event with an invalid token")
//	public void userCreatesEventWithInvalidToken() {
//
//		String requestBody = """
//				{
//				  "title": "Tech Summit 2026",
//				  "description": "Technology conference",
//				  "category": "Conference",
//				  "venue": "Bangalore International Centre",
//				  "city": "Bangalore",
//				  "eventDate": "2026-09-15T09:00:00.000Z",
//				  "price": 1500,
//				  "totalSeats": 500,
//				  "imageUrl": "https://example.com/images/event.jpg"
//				}
//				""";
//
//		response = createEventsService.createEventWithInvalidToken(requestBody);
//	}
//
//	@When("the user creates an event with an empty request body")
//	public void userCreatesEventWithEmptyRequestBody() {
//
//		response = createEventsService.createEvent("{}");
//	}
//
//	@When("the user creates an event without title")
//	public void userCreatesEventWithoutTitle() {
//
//		String requestBody = """
//				{
//				  "description": "Technology conference",
//				  "category": "Conference",
//				  "venue": "Bangalore International Centre",
//				  "city": "Bangalore",
//				  "eventDate": "2026-09-15T09:00:00.000Z",
//				  "price": 1500,
//				  "totalSeats": 500,
//				  "imageUrl": "https://example.com/images/event.jpg"
//				}
//				""";
//
//		response = createEventsService.createEvent(requestBody);
//	}
//
//	@When("the user creates an event with negative price")
//	public void userCreatesEventWithNegativePrice() {
//
//		String requestBody = """
//				{
//				  "title": "Tech Summit 2026",
//				  "description": "Technology conference",
//				  "category": "Conference",
//				  "venue": "Bangalore International Centre",
//				  "city": "Bangalore",
//				  "eventDate": "2026-09-15T09:00:00.000Z",
//				  "price": -100,
//				  "totalSeats": 500,
//				  "imageUrl": "https://example.com/images/event.jpg"
//				}
//				""";
//
//		response = createEventsService.createEvent(requestBody);
//	}
//
//	@When("the user creates an event with negative total seats")
//	public void userCreatesEventWithNegativeTotalSeats() {
//
//		String requestBody = """
//				{
//				  "title": "Tech Summit 2026",
//				  "description": "Technology conference",
//				  "category": "Conference",
//				  "venue": "Bangalore International Centre",
//				  "city": "Bangalore",
//				  "eventDate": "2026-09-15T09:00:00.000Z",
//				  "price": 1500,
//				  "totalSeats": -500,
//				  "imageUrl": "https://example.com/images/event.jpg"
//				}
//				""";
//
//		response = createEventsService.createEvent(requestBody);
//	}
//
//	@Then("the validation error should contain field {string}")
//	public void validationErrorShouldContainField(String expectedField) {
//
//		String field = response.jsonPath()
//				.getString("details.find { it.field == '%s' }.field".formatted(expectedField));
//
//		assertEquals(field, expectedField, "Expected validation error for field: " + expectedField);
//	}
//}