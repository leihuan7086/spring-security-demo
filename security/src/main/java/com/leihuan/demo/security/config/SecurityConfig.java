package com.leihuan.demo.security.config;

import com.leihuan.demo.security.security.authentication.CustomAuthenticationFilter;
import com.leihuan.demo.security.security.authentication.CustomAuthenticationProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        // 一般配置忽略静态资源url
        web.ignoring().antMatchers("/login.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);// 自定义配置，必须删掉super.configure(http);
        http.csrf().disable(); //禁用CSRF保护
        http.httpBasic(); //启用Basic认证
        http.authorizeRequests()
                .antMatchers("/debug/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(this.customAuthenticationFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManager authenticationManager = super.authenticationManager();
        if (authenticationManager instanceof ProviderManager) {
            List<AuthenticationProvider> providers = ((ProviderManager) authenticationManager).getProviders();
            // 初始化时默认官方Provider只有DaoAuthenticationProvider
            if (providers.size() <= 1) {
                providers.add(this.customAuthenticationProvider());
            }
        }
        return authenticationManager;
    }

    // 定义private，防止外部调用
    private CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter("/login");
        filter.setAuthenticationManager(this.authenticationManager());
        return filter;
    }

    // 定义private，防止外部调用
    private CustomAuthenticationProvider customAuthenticationProvider() {
        CustomAuthenticationProvider provider = new CustomAuthenticationProvider();
        return provider;
    }

}
