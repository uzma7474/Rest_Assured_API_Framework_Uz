package models.response;

public class AuthResponse {
	private boolean success;
	
	private UserAuthResponse userAuthResponse;
	
	public AuthResponse() {
		
	}
	
	public AuthResponse(boolean success, UserAuthResponse userAuthResponse) {
		this.success = success;
		this.userAuthResponse = userAuthResponse;
	}

	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public UserAuthResponse getUserAuthResponse() {
		return userAuthResponse;
	}

	public void setUserAuthResponse(UserAuthResponse userAuthResponse) {
		this.userAuthResponse = userAuthResponse;
	}


}
