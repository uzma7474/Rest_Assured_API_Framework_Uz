package services;

import static io.restassured.RestAssured.given;

import base.BookingBaseApi;
import config.ConfigManager;
import endpoints.BookingEndpoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.response.GetBookingByIdResponse;

public class GetBookingByIdService extends BookingBaseApi {

	private RequestSpecification requestSpecification;
	// "application/json; charset=utf-8"
	private ContentType contentType;

	public GetBookingByIdService() {
		this.requestSpecification = requestSpecForGetBookingIds();

	}

//	public Response getBookingById(Integer id, ContentType contentType) {
//
//		return given()
//					.spec(requestSpecification)
//					.header("Accept", contentType.toString())
//					.pathParam("id", id)
//				.when()
//					.get(BookingEndpoints.BOOKING + "/{id}");
//	}

	public Response getBookingById(Integer bookingId, ContentType contentType) {

		RequestSpecification request = given().spec(requestSpecification).contentType(contentType)
				.accept(ContentType.JSON);

		// Empty/null booking ID
		if (bookingId == null) {

			return request.when().get(BookingEndpoints.BOOKING + "/").then().extract().response();
		}

		// Valid booking ID
		return request.pathParam("bookingId", bookingId).when().get(BookingEndpoints.BOOKING + "/{bookingId}").then()
				.extract().response();
	}

	public Response getBookingByEndpoint(String endpoint, ContentType contentType) {

		return given()
					.spec(requestSpecification)
					.contentType(contentType)
			  .when()
			  	.get(endpoint);
	}

	public GetBookingByIdResponse getBookingByIdApiResponse(Response res) {
		Response response = res.then().extract().response();

		if (response.statusCode() == 200) {
			return response.as(GetBookingByIdResponse.class);
		}

		return null;
	}

}
