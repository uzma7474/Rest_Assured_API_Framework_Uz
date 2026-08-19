package models.response;

import java.util.List;

public class LoginErrorResponse extends BaseErrorResponse{
	private List<Details> details;

	public LoginErrorResponse() {
	}

	public LoginErrorResponse(boolean success, String error, List<Details> details) {

		
		this.details = details;
	}


	public List<Details> getDetails() {
		return details;
	}

	public void setDetails(List<Details> details) {
		this.details = details;
	}

//	@Override
//	public String toString() {
//		return "RegisterErrorResponse{" + "success=" + success + ", error='" + error + '\'' + ", details=" + details
//				+ '}';
//	}
}