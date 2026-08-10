package utils;

/**
 * TokenManager
 *
 * Centralized manager for authentication tokens.
 *
 * Responsibilities: - Store authentication token - Retrieve authentication
 * token - Clear authentication token - Support parallel execution using
 * ThreadLocal
 *
 * ThreadLocal is used so that parallel Cucumber/TestNG scenarios do not
 * overwrite each other's tokens.
 */
public final class TokenManager {

	private TokenManager() {
		// Prevent object creation
	}

	/**
	 * Stores the authentication token for the current thread.
	 */
	private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();

	/**
	 * Stores authentication token.
	 *
	 * @param token authentication token
	 */
	public static void setToken(String token) {

		if (token == null || token.isBlank()) {

			throw new IllegalArgumentException("Authentication token cannot be null or empty.");
		}

		TOKEN.set(token);
	}

	/**
	 * Returns authentication token for the current thread.
	 *
	 * @return authentication token
	 */
	public static String getToken() {

		return TOKEN.get();
	}

	/**
	 * Checks whether an authentication token exists.
	 *
	 * @return true if token exists, otherwise false
	 */
	public static boolean hasToken() {

		String token = TOKEN.get();

		return token != null && !token.isBlank();
	}

	/**
	 * Returns token with Bearer prefix.
	 *
	 * Example:
	 *
	 * token123
	 *
	 * becomes:
	 *
	 * Bearer token123
	 *
	 * @return Bearer authentication token
	 */
	public static String getBearerToken() {

		String token = getToken();

		if (token == null || token.isBlank()) {

			throw new IllegalStateException("Authentication token is not available.");
		}

		return "Bearer " + token;
	}

	/**
	 * Clears authentication token for the current thread.
	 *
	 * remove() is preferred over set(null) because it prevents ThreadLocal values
	 * from remaining attached to reused threads.
	 */
	public static void clear() {

		TOKEN.remove();
	}
}
