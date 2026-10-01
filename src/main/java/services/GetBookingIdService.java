package services;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import base.BookingBaseApi;
import config.ConfigManager;
import endpoints.BookingEndpoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.requests.AuthBookingRequest;

public class GetBookingIdService extends BookingBaseApi {
	
	private RequestSpecification requestSpecification;
	
	private ContentType contentType;
	
	public GetBookingIdService() {
		this.requestSpecification = getBookingRequestSpec();
		
	}
	
	public Response getBookingIdsWithNoQuery() {

		return given()
					.header("Content-Type", "application/json; charset=UTF-8")
					.spec(requestSpecification)
				.when()
					.get(BookingEndpoints.BOOKING);
	}
	
	
	public Response getBookingIds() {

		return given()
					.header("Content-Type", "application/json; charset=UTF-8")
					.spec(requestSpecification)
					.queryParam("firstname", ConfigManager.getProperty("firstname"))
					.queryParam("lastname", ConfigManager.getProperty("lastname"))
				.when()
					.get(BookingEndpoints.BOOKING);
	}
	
	public Response getBookingIdsByFirstname(String firstname) {
		return given()
				.header("Content-Type", "application/json; charset=UTF-8")
				.spec(requestSpecification)
				.queryParam("firstname", firstname)
			.when()
				.get(BookingEndpoints.BOOKING);
	}
	
	public Response getBookingIdsByLastname(String lastname) {
		return given()
				.header("Content-Type", "application/json; charset=UTF-8")
				.spec(requestSpecification)
				.queryParam("lastname", lastname)
			.when()
				.get(BookingEndpoints.BOOKING);
	}

	public Response getBookingIdsWithParams(String firstname, String lastname, String checkin, String checkout) {

	    // Create a map to dynamically store active query parameters
	    Map<String, String> queryParams = new HashMap<>();
	    
	    // Only add parameters that are not null and not blank
	    if (firstname != null && !firstname.trim().isEmpty()) queryParams.put("firstname", firstname);
	    if (lastname != null && !lastname.trim().isEmpty())   queryParams.put("lastname", lastname);
	    if (checkin != null && !checkin.trim().isEmpty())     queryParams.put("checkin", checkin);
	    if (checkout != null && !checkout.trim().isEmpty())   queryParams.put("checkout", checkout);

		return given()
					.header("Content-Type", "application/json; charset=UTF-8")
					.spec(requestSpecification)
					.queryParams(queryParams)
				.when()
					.get(BookingEndpoints.BOOKING);
	}	
	
	public Response getBookingIdsWithoutParams() {

		return given()
					.header("Content-Type", "application/json; charset=UTF-8")
					.spec(requestSpecification)
		
				.when()
					.get(BookingEndpoints.BOOKING);
	}		
	
	
    // ============================================================
    // GET /booking/{bookingId}
    // ============================================================
	
	public Response getBookingById(Integer bookingId) {

        return given()
                .spec(requestSpecification)
                .header("Accept", "application/json")
                .pathParam("bookingId", bookingId)

            .when()
                .get("/booking/{bookingId}")

            .then()
                .extract()
                .response();
    }
	
	
	
	
}
