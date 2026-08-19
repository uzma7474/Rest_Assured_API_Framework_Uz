package services;

import base.BaseApi;
import endpoints.AuthEndpoints;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.response.AuthMeResponse;
import models.response.BaseErrorResponse;

import static io.restassured.RestAssured.given;

public class AuthMeService extends BaseApi {

    private final RequestSpecification requestSpecification;

    public AuthMeService() {
        this.requestSpecification = getRequestSpec();
    }

    /**
     * GET /api/auth/me
     *
     * @param authorizationValue
     *        null  -> Authorization header is not sent
     *        ""    -> empty Authorization header
     *        other -> exact Authorization header value supplied
     */
    public Response getCurrentUserAuthMe(String authorizationValue) {

        RequestSpecification request = given()
							                .spec(requestSpecification)
							                .header("Accept", "application/json");

        /*
         * Important:
         *
         * Do NOT automatically add TokenManager.getToken().
         *
         * The caller decides exactly what Authorization header
         * should be sent.
         */
        if (authorizationValue != null) {
            request.header("Authorization", authorizationValue);
        }

        return request
                .when()
                		.get(AuthEndpoints.AuthMe)
                .then()
                		.extract()
                		.response();
    }


    /**
     * GET /api/auth/me using the currently stored valid token.
     *
     * Used by positive scenarios when no custom Authorization
     * header has been specified.
     */
    public Response getCurrentUserAuthMeWithValidToken() {

        String token = utils.TokenManager.getToken();

        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "Token is null or empty. Login must be executed before calling /auth/me."
            );
        }

        return getCurrentUserAuthMe("Bearer " + token);
    }


    /**
     * Convert successful Auth Me response to POJO.
     */
    public AuthMeResponse getAuthMeResponse(Response response) {

        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null");
        }

        return response.as(AuthMeResponse.class);
    }


    /**
     * Convert error response to BaseErrorResponse.
     */
    public BaseErrorResponse getErrorResponse(Response response) {

        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null");
        }

        return response.as(BaseErrorResponse.class);
    }
    
    
    public Response getCurrentUserAuthMeWithoutAuthorization(String authEndpoint) {

        return given()
                .spec(requestSpecification)
                .header("Accept", "application/json")
            .when()
                .get(authEndpoint);
    }
    
    
    
}