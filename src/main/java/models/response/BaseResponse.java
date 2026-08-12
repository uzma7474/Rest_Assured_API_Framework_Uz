package models.response;

import java.util.List;

public class BaseResponse {
	private boolean success;
	private String error;
	
	public BaseResponse() {
	}

	public BaseResponse(boolean success, String error) {

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
