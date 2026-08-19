package constants;

/**
 * Contains API-level constants used throughout the REST Assured automation
 * framework.
 */
public final class ApiConstants {

	private ApiConstants() {
		// Prevent object creation
	}

	// ==============================
	// API Version
	// ==============================

	public static final String API_V1 = "/api/v1";

	public static final String API_V2 = "/api/v2";

	// ==============================
	// Authentication
	// ==============================

	public static final String AUTH = "/api/auth";
	
	public static final String REGISTER = AUTH + "/register";
	
	public static final String ME = AUTH + "/me";

	public static final String LOGIN = AUTH + "/login";

	public static final String LOGOUT = AUTH + "/logout";

	public static final String REFRESH_TOKEN = AUTH + "/refresh-token";

	// ==============================
	// Users
	// ==============================

	public static final String USERS = "/users";

	public static final String USER_BY_ID = USERS + "/{id}";

	// ==============================
	// Products
	// ==============================

	public static final String PRODUCTS = "/products";

	public static final String PRODUCT_BY_ID = PRODUCTS + "/{id}";

	// ==============================
	// Orders
	// ==============================

	public static final String ORDERS = "/orders";

	public static final String ORDER_BY_ID = ORDERS + "/{id}";

	// ==============================
	// Headers
	// ==============================

	public static final String HEADER_CONTENT_TYPE = "Content-Type";

	public static final String HEADER_ACCEPT = "Accept";

	public static final String HEADER_AUTHORIZATION = "Authorization";

	public static final String HEADER_USER_AGENT = "User-Agent";

	// ==============================
	// Content Types
	// ==============================

	public static final String APPLICATION_JSON = "application/json";

	public static final String APPLICATION_XML = "application/xml";

	public static final String TEXT_PLAIN = "text/plain";

	// ==============================
	// Authentication Prefix
	// ==============================

	public static final String BEARER = "Bearer ";
}
