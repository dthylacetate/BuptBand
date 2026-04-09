package com.bupt.BuptBand.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 允许跨域的 Cookie
        config.setAllowCredentials(true);
        // 允许前端的来源（注意：如果你用了 .env，确保这里的地址和前端一致）
        config.addAllowedOriginPattern("*");
        // 允许任何请求头（这一步最重要，因为我们要传 Authorization）
        config.addAllowedHeader("*");
        // 允许任何方法（GET, POST, OPTIONS, DELETE 等）
        config.addAllowedMethod("*");

        // 对所有接口生效
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}