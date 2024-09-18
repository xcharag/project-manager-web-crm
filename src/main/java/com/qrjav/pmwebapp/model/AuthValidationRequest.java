package com.qrjav.pmwebapp.model;

import lombok.Data;

@Data
public class AuthValidationRequest {
    private String username;
    private String token;

    public AuthValidationRequest(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
