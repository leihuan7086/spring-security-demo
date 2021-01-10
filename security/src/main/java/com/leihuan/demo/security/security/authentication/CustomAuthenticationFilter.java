package com.leihuan.demo.security.security.authentication;

import com.leihuan.demo.security.security.exception.UserAuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public CustomAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        System.out.println("request : " + ((HttpServletRequest) request).getRequestURI());
        super.doFilter(request, response, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 根据request构建认证Token
        AbstractAuthenticationToken authRequest = this.obtainAuthenticationToken(request);

        // 已经认证用户不再认证
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (existingAuth instanceof CustomAuthenticationToken && existingAuth.isAuthenticated() && existingAuth.getName().equals(authRequest.getName())) {
//            System.out.println("username : " + existingAuth.getName());
            return existingAuth;
        }

        // 执行认证
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    // 1.Basic认证
    // 2.表单认证
    private AbstractAuthenticationToken obtainAuthenticationToken(HttpServletRequest request) throws UnsupportedEncodingException {
        String username = null;
        String password = null;

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!ObjectUtils.isEmpty(authorization) && authorization.startsWith("Basic ")) {
            byte[] base64Token = authorization.substring(6).getBytes("UTF-8");
            String token = new String(Base64.getDecoder().decode(base64Token), "UTF-8");
            String[] split = token.split(":");
            if (split.length == 2) {
                username = split[0];
                password = split[1];
            }
        } else if (parameterMap.containsKey("username") && parameterMap.containsKey("password")) {
            username = request.getParameter("username");
            password = request.getParameter("password");
        }

        if (username != null && password != null) {
            CustomAuthenticationToken authRequest = new CustomAuthenticationToken();
            authRequest.setPrincipal(username);
            authRequest.setCredentials(password);
            authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
            return authRequest;
        }

        throw new UserAuthenticationException("Authorization: " + authorization);
    }
}