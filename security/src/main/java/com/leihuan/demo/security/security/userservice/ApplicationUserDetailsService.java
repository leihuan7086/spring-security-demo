package com.leihuan.demo.security.security.userservice;

import com.leihuan.demo.security.security.exception.UserAuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

// 如需自定义UserDetailsService，实现UserDetailsService即可
// 源代码在InitializeUserDetailsBeanManagerConfigurer.InitializeUserDetailsManagerConfigurer.configure()方法中
// 修改SpringSecurity默认DaoAuthenticationProvider的UserDetailsService的两种方式：
// 1. ApplicationUserDetailsService添加@Service注解注入到Spring容器
// 2. SecurityConfig.configure(AuthenticationManagerBuilder auth)添加自定义配置
// auth.userDetailsService(new ApplicationUserDetailsService())
@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    // 1，密码默认使用bcrypt方式加密
    // 2，如果密码不加密，Spring容器需要实例化DelegatingPasswordEncoder，并设置defaultPasswordEncoderForMatches为NoOpPasswordEncoder
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // 一般设计为从数据库读取用户
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.contains("admin") || username.contains("root")) {
            return new User(username, passwordEncoder.encode(username + "Aa123456"), Collections.EMPTY_LIST);
        }

        throw new UserAuthenticationException("username: " + username);
    }

}
