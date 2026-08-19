package docs;

import static io.restassured.RestAssured.given;

import base.BaseApi;
import base.RequestSpecificationFactory;
import endpoints.EventsEnpoints;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.TokenManager;


public class CreateEventService extends BaseApi{

	private final RequestSpecification requestSpecification;

    public CreateEventService() {
        this.requestSpecification = getRequestSpec();
    }



    /**
     * Create event with valid authentication.
     */
    public Response createEvent(CreateEventRequest request) {

        return given()
                .spec(requestSpecification)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .body(request)
            .when()
                .post(EventsEnpoints.EVENTS);
    }

    /**
     * Create event using raw JSON.
     */
    public Response createEvent(String requestBody) {

        return given()
                .spec(requestSpecification)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .body(requestBody)
            .when()
                .post(EventsEnpoints.EVENTS);
    }

    /**
     * Create event without Authorization header.
     */
    public Response createEventWithoutAuthorization(String requestBody) {

        return given()
                .spec(requestSpecification)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
            .when()
                .post(EventsEnpoints.EVENTS);
    }

    /**
     * Create event with invalid token.
     */
    public Response createEventWithInvalidToken(String requestBody) {

        return given()
                .spec(requestSpecification)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer invalid-token")
                .body(requestBody)
            .when()
                .post(EventsEnpoints.EVENTS);
    }

    /**
     * Create event without Content-Type.
     */
    public Response createEventWithoutContentType(String requestBody) {

        return given()
                .spec(requestSpecification)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .body(requestBody)
            .when()
                .post(EventsEnpoints.EVENTS);
    }

    /**
     * Create event with custom Content-Type.
     */
    public Response createEventWithContentType(
            String requestBody,
            String contentType) {

        return given()
                .spec(requestSpecification)
                .header("Accept", "application/json")
                .header("Content-Type", contentType)
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .body(requestBody)
            .when()
                .post(EventsEnpoints.EVENTS);
    }
}