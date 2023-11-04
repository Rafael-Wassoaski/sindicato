package com.rafaelwassoaski.sindicato.dto;

public class TokenDTO {
    private String username;
    private String jwtToken;

    public TokenDTO(String username, String jwtToken) {
        this.username = username;
        this.jwtToken = jwtToken;
    }

    public TokenDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}