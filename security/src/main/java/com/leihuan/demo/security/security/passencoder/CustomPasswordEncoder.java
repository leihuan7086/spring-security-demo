package com.leihuan.demo.security.security.passencoder;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

// 如需自定义PasswordEncoder，实现PasswordEncoder即可
// 修改SpringSecurity默认DaoAuthenticationProvider的PasswordEncoder的两种方式：
// 1. CustomPasswordEncoder添加@Service注解注入到Spring容器
// 2. SecurityConfig.configure(AuthenticationManagerBuilder auth)添加自定义配置
// auth.userDetailsService(new ApplicationUserDetailsService()).passwordEncoder(new CustomPasswordEncoder())
public class CustomPasswordEncoder implements PasswordEncoder {

    public static String PREFIX = "[custom]";

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return PREFIX + passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (!encodedPassword.startsWith(PREFIX)) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword.substring(PREFIX.length()));
    }
}
