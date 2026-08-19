package context;

import io.restassured.response.Response;
import models.response.BaseErrorResponse;

public class ScenarioContext {
	
	private Response response;
	
	private BaseErrorResponse responseError;

    private String token;
    
    private Integer loginUserId;
    private Integer authMeUserId;

    private String loginEmail;
    private String authMeEmail;

    public Response getResponse() {
        return response;
    }
    


    public void setResponse(Response response) {
        this.response = response;
    }

    public BaseErrorResponse getBaseErrorResponse() {
        return responseError;
    }
    
    public void setBaseErrorResponse(BaseErrorResponse response) {
        this.responseError = response;
    }

    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    
    public Integer getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Integer loginUserId) {
        this.loginUserId = loginUserId;
    }

    public Integer getAuthMeUserId() {
        return authMeUserId;
    }

    public void setAuthMeUserId(Integer authMeUserId) {
        this.authMeUserId = authMeUserId;
    }
    
    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }
    
    public String getAuthMeEmail() {
        return authMeEmail;
    }

    public void setAuthMeEmail(String authMeEmail) {
        this.authMeEmail = authMeEmail;
    }

}
