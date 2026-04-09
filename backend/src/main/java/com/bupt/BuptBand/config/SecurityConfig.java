package com.bupt.BuptBand.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Configuration
public class SecurityConfig
{

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter)
    {
        this.jwtFilter = jwtFilter;
    }




    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/doc.html")
                .requestMatchers("/webjars/**")
                .requestMatchers("/v3/api-docs/**") // 匹配所有 v3 开头的文档数据
                .requestMatchers("/v3/api-docs")    // 匹配精确路径
                .requestMatchers("/swagger-resources/**")
                .requestMatchers("/swagger-ui/**")
                .requestMatchers("/favicon.ico");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .cors(Customizer.withDefaults()) // 开启跨域支持
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(f -> f.sameOrigin()))
                .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/api/users/register",
                                "/api/users/login",
                                "/api/users/send-code",
                                "/h2-console/**",
                                "/doc.html",
                                "/webjars/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources/**").permitAll()
                        .anyRequest().authenticated() // ！！！除了上面的，其他全部要求带 Token 登录
                );
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        // 工业界公认最安全的加密方案之一
        return new BCryptPasswordEncoder();
    }

    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(csrf -> csrf.disable()) // 必须禁用！否则 POST/PUT 会报 403 或 401

                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().permitAll() // 暂时允许所有，方便开发调试
                );
        return http.build();
    }

    */

}