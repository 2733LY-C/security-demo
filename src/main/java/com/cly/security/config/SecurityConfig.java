package com.cly.security.config;

import com.cly.security.filter.JwtAuthenticationTokenFilter;
import com.cly.security.handle.AccessDeniedHandlerImpl;
import com.cly.security.handle.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    //配置  1.登录放行
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AccessDeniedHandlerImpl accessDeniedHandlerImpl, AuthenticationEntryPointImpl authenticationEntryPointImpl) throws Exception {
        //配置 关闭csrf机制:禁用Spring Security的CSRF（跨站请求伪造）保护机制
        http
                .csrf(csrf -> csrf.disable())
        //配置全球拦截方式  permitAll: 随意访问

                .authorizeHttpRequests(auth -> auth.requestMatchers("/user/login").permitAll()
                .anyRequest().authenticated())

           // 把token校验过滤器添加到过滤器链中
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)


        /*在UsernamePasswordAuthenticationFilter前面添加自定义的过滤器*/
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                /*添加自定义没有权限处理器*/
                .exceptionHandling(configurer -> configurer.accessDeniedHandler(accessDeniedHandlerImpl)
                        .authenticationEntryPoint(authenticationEntryPointImpl));

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录时需要调用AuthenticationManager.authenticate执行一次校验
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }




}
