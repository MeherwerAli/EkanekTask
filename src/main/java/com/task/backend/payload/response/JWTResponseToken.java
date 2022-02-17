package com.task.backend.payload.response;

public class JWTResponseToken {

    private String userName;
    private String jwtToken;
    private String message;

    public JWTResponseToken(String userName, String jwtToken) {
        this.userName = userName;
        this.jwtToken = jwtToken;
    }

    public JWTResponseToken(String userName, String jwtToken, String message) {
        this.userName = userName;
        this.jwtToken = jwtToken;
        this.message = message;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
