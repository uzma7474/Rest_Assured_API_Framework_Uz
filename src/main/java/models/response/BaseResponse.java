package models.response;

public class BaseResponse {

	private boolean success;
	
	// private UserAuthResponse userAuth;
	
	
	public BaseResponse() {
		
	}
	
	public BaseResponse(boolean success) {
		 this.success = success;
	   //  this.userAuth = user;
	}
	
	
//	public BaseResponse(boolean success, UserAuthResponse user) {
//		 this.success = success;
//	     this.userAuth = user;
//	}
//	
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

//	public UserAuthResponse getUser() {
//		return userAuth;
//	}
//
//	public void setUser(UserAuthResponse user) {
//		this.userAuth = user;
//	}


	
}
