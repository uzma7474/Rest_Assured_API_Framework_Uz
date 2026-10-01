package services;

import static io.restassured.RestAssured.given;

import base.BookingBaseApi;
import endpoints.BookingEndpoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.requests.Booking;
import models.response.CreateBookingResponse;

public class CreateBookingService extends BookingBaseApi {

	private RequestSpecification requestSpecification;
	// "application/json; charset=utf-8"
	private ContentType contentType;

	public CreateBookingService() {
		this.requestSpecification = requestSpecForCreateBooking();
	}
	
	public Response createBooking() {

		return given().spec(requestSpecification).when().post(BookingEndpoints.BOOKING);
	}

	public Response createBooking_not_using(String contentType, String acceptHeader, Booking booking) {

		RequestSpecification requestSpec = given();

		// Only add Content-Type if it isn't null
		if (contentType != null && !contentType.trim().isEmpty()) {
			requestSpec.contentType(contentType);
		}

		// Add Accept header only when it is explicitly provided
		if (acceptHeader != null && !acceptHeader.trim().isEmpty()) {
			requestSpec.accept(acceptHeader);
		}

		return requestSpec.spec(requestSpecification).body(booking).when().post(BookingEndpoints.BOOKING);
	}



	public Response createBooking_(String contentType, String acceptHeader, Booking booking) {

		RequestSpecification requestSpec = given().spec(requestSpecification);

		// Add Content-Type only when provided
		if (contentType != null && !contentType.trim().isEmpty()) {
			requestSpec.contentType(contentType);
		}

		// Add Accept header only when provided
		if (acceptHeader != null && !acceptHeader.trim().isEmpty()) {
			requestSpec.accept(acceptHeader);
		}

		// Add request body only when booking object is provided
		if (booking != null) {
			requestSpec.body(booking);
		}

		return requestSpec.when().post(BookingEndpoints.BOOKING);
	}
	
	//=============================================================================================
	// New added on 25/09/2026
	//=============================================================================================
	 public Response createBooking_not_using_( String contentType, String acceptHeader, Booking booking) {
	            
	        RequestSpecification requestSpec =
	                given()
	                    .baseUri("https://restful-booker.herokuapp.com")
	                    .contentType(ContentType.JSON)
	                    .accept(ContentType.JSON);

	        System.out.println("\n========== ACTUAL HTTP REQUEST ==========");

	        return requestSpec
	                .body(booking)
	                .log()
	                .all()
	                .when()
	                .post(BookingEndpoints.BOOKING)
	                .then()
	                .log()
	                .all()
	                .extract()
	                .response();
	    }
	 
	 
	 public Response createBooking(String contentType, String acceptHeader, Booking booking) {

	        Response response = given()
	                .baseUri("https://restful-booker.herokuapp.com")
	                .header("Accept", "application/json")
	                .header("Content-Type", "application/json")
	                .body(booking)
	                .log()
	                .all()
	                .when()
	                .post(BookingEndpoints.BOOKING)
	                .then()
	                .log()
	                .all()
	                .extract()
	                .response();

	        return response;
	    }
	
	
	 /**
     * Create booking with default request specification.
     */
    public Response createBooking(Booking booking) {

        return given()
                .spec(requestSpecification)
                .body(booking)
                .when()
                .post(BookingEndpoints.BOOKING);
    }

    /**
     * Convert successful response into CreateBookingResponse POJO.
     */
    public CreateBookingResponse getCreateBookingResponse(Response response) {

        if (response == null) {
            throw new IllegalArgumentException(
                    "Response cannot be null");
        }

        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "Cannot deserialize unsuccessful response. " +
                    "Expected HTTP 200 but received HTTP "
                    + response.statusCode() +
                    ". Response body: " +
                    response.asPrettyString());
        }

        return response.as(CreateBookingResponse.class);
    }

	public CreateBookingResponse createBookingApiResponse(Response res) {
		Response response = res.then().extract().response();

		if (response == null) {
			throw new IllegalArgumentException("Response cannot be null");
		}

		if (response.statusCode() == 200) {
			return response.as(CreateBookingResponse.class);
		}

		throw new AssertionError("Expected HTTP 200 but received " + response.statusCode() + ". Response body: "
				+ response.asPrettyString());
	}

}
