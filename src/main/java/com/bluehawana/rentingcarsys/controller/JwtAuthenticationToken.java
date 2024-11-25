package com.bluehawana.rentingcarsys.controller;

public class JwtAuthenticationToken
{
    private String token;

    public JwtAuthenticationToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
