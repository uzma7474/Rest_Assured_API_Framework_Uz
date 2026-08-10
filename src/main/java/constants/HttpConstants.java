package constants;

/**
 * Contains HTTP-related constants used throughout the REST Assured automation
 * framework.
 */
public final class HttpConstants {

	private HttpConstants() {
		// Prevent object creation
	}

	// ==============================================================
	// HTTP Methods
	// ==============================================================

	public static final String GET = "GET";

	public static final String POST = "POST";

	public static final String PUT = "PUT";

	public static final String PATCH = "PATCH";

	public static final String DELETE = "DELETE";

	public static final String HEAD = "HEAD";

	public static final String OPTIONS = "OPTIONS";

	// ==============================================================
	// Success Status Codes
	// ==============================================================

	public static final int OK = 200;

	public static final int CREATED = 201;

	public static final int ACCEPTED = 202;

	public static final int NO_CONTENT = 204;

	// ==============================================================
	// Redirection Status Codes
	// ==============================================================

	public static final int MOVED_PERMANENTLY = 301;

	public static final int FOUND = 302;

	public static final int NOT_MODIFIED = 304;

	// ==============================================================
	// Client Error Status Codes
	// ==============================================================

	public static final int BAD_REQUEST = 400;

	public static final int UNAUTHORIZED = 401;

	public static final int FORBIDDEN = 403;

	public static final int NOT_FOUND = 404;

	public static final int METHOD_NOT_ALLOWED = 405;

	public static final int CONFLICT = 409;

	public static final int UNPROCESSABLE_ENTITY = 422;

	public static final int TOO_MANY_REQUESTS = 429;

	// ==============================================================
	// Server Error Status Codes
	// ==============================================================

	public static final int INTERNAL_SERVER_ERROR = 500;

	public static final int NOT_IMPLEMENTED = 501;

	public static final int BAD_GATEWAY = 502;

	public static final int SERVICE_UNAVAILABLE = 503;

	public static final int GATEWAY_TIMEOUT = 504;

	// ==============================================================
	// Common Status Code Messages
	// ==============================================================

	public static final String SUCCESS_MESSAGE = "Request executed successfully";

	public static final String BAD_REQUEST_MESSAGE = "Bad request";

	public static final String UNAUTHORIZED_MESSAGE = "Unauthorized";

	public static final String FORBIDDEN_MESSAGE = "Forbidden";

	public static final String NOT_FOUND_MESSAGE = "Resource not found";

	public static final String SERVER_ERROR_MESSAGE = "Internal server error";
}
