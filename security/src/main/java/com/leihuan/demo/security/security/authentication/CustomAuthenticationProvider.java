package com.leihuan.demo.security.security.authentication;

import com.leihuan.demo.security.security.exception.UserAuthenticationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // supports(Class<?> authentication)返回true，即自定义Token匹配成功才会执行此认证方法
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        if ((username.contains("admin") || username.contains("root")) && "Aa123456".equals(password)) {
            CustomAuthenticationToken customAuthenticationToken = new CustomAuthenticationToken();
            User user = new User(username, password, authentication.getAuthorities());
            customAuthenticationToken.setPrincipal(user);
            customAuthenticationToken.setAuthenticated(true);
            customAuthenticationToken.setDetails(authentication.getDetails());
            return customAuthenticationToken;
        }

        throw new UserAuthenticationException("username:" + username);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
