package models.response;

public class AuthMeResponse {
    private boolean success;
    private String token;
    private UserAuthResponse user;

    public AuthMeResponse() {
    }

    public AuthMeResponse(boolean success, String token, UserAuthResponse user) {
        this.success = success;
        this.token = token;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserAuthResponse getUser() {
        return user;
    }

    public void setUser(UserAuthResponse user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "success=" + success +
                ", token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}