package com.leihuan.demo.security.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private Object principal;

    private String credentials;

    public CustomAuthenticationToken() {
        super(null);
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }
}
