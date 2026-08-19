package models.response;

import java.util.List;

public class BaseErrorResponse {
	public boolean success;
	public String error;
	
	public BaseErrorResponse() {
	}

	public BaseErrorResponse(boolean success, String error) {

		this.success = success;
		this.error = error;
		
	}
	
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
}
