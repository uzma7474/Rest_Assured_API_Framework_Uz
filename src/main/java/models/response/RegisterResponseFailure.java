package models.response;

public class RegisterResponseFailure {
	
	private boolean success;
	private String error;
	private Details details;
	
	public RegisterResponseFailure() {
    }

    public RegisterResponseFailure(boolean success, String error, Details details) {
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
	public Details getDetails() {
		return details;
	}
	public void setDetails(Details details) {
		this.details = details;
	}
	
	@Override
    public String toString() {
        return "RegisterResponse{" +
                "success=" + success +
                ", error='" + error + '\'' +
                ", details=" + details +
                '}';
    }

}
