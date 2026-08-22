package models.response;

public class AuthBookingResponse {
	private String token;

	// Default constructor
	public AuthBookingResponse() {
	}

	// Parameterized constructor
	public AuthBookingResponse(String token) {
		this.token = token;

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
