package endpoints;

/**
 * Contains all Authentication API endpoints.
 */
public final class AuthEndpoints {

	private AuthEndpoints() {
		// Prevent object creation
	}

	public static final String REGISTER = "/api/auth/register";
	
	
	/**
	 * Login endpoint. POST /auth/login
	 */
	public static final String LOGIN = "/api/auth/login";

	
	/**
	 * Auth me endpoint
	 */
	public static final String AuthMe = "/api/auth/me";
	
	
	/**
	 * Logout endpoint. POST /auth/logout
	 */
	public static final String LOGOUT = "/api/auth/logout";

	/**
     * Refresh token endpoint.
     * POST /auth/refresh-token
     */
    public static final String REFRESH_TOKEN ="/api/auth/refresh-token";
}
