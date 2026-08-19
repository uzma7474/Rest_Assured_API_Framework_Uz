package models.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuthResponse {

	 @JsonAlias({"id", "userId"})
    private int userId;
    private String email;
    private long iat;
    private long exp;

    // Default constructor
    public UserAuthResponse() {
    }

    // Parameterized constructor
    public UserAuthResponse(int userId, String email, long iat, long exp) {
        this.userId = userId;
        this.email = email;
        this.iat = iat;
        this.exp = exp;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getIat() {
        return iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", iat=" + iat +
                ", exp=" + exp +
                '}';
    }
}
