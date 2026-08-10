package models.requests;

public class RegisterRequest {

	private String email;
	private String password;

	// Default constructor
	public RegisterRequest() {
	}

	// Parameterized constructor
	public RegisterRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	// Getter
	public String getEmail() {
		return email;
	}

	// Setter
	public void setEmail(String email) {
		this.email = email;
	}

	// Getter
	public String getPassword() {
		return password;
	}

	// Setter
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "RegisterRequest{" + "email='" + email + '\'' + ", password='" + password + '\'' + '}';
	}
}