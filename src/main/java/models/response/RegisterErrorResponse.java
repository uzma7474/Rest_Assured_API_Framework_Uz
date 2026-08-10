package models.response;

import java.util.List;

public class RegisterErrorResponse {
	private boolean success;
	private String error;
	private List<Details> details;

	public RegisterErrorResponse() {
	}

	public RegisterErrorResponse(boolean success, String error, List<Details> details) {

		this.success = success;
		this.error = error;
		this.details = details;
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

	public List<Details> getDetails() {
		return details;
	}

	public void setDetails(List<Details> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "RegisterErrorResponse{" + "success=" + success + ", error='" + error + '\'' + ", details=" + details
				+ '}';
	}
}