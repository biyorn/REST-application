package com.epam.esm.security;

import java.io.Serializable;

public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;
    private String login;
    private String password;
    private String refreshToken;

    public JwtRequest() {
    }

    public JwtRequest(String login, String password, String refreshToken) {
        this.login = login;
        this.password = password;
        this.refreshToken = refreshToken;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
