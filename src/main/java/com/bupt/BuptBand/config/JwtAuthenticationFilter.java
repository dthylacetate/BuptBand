package com.bupt.BuptBand.config;

import com.bupt.BuptBand.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil)
    {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        //从请求头里找 Authorization: Bearer <TOKEN>
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer "))
        {
            String token = authHeader.substring(7);
            try
            {
                // 解析 Token 拿到昵称
                String nickname = jwtUtil.extractNickname(token);

                if (nickname != null && SecurityContextHolder.getContext().getAuthentication() == null)
                {
                    // 把身份信息塞进 Spring 的安全上下文中
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(nickname, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            catch (Exception e)
            {
                // Token 无效或过期，这里可以选择记录日志
            }
        }

        // 放行给下一个环节
        filterChain.doFilter(request, response);
    }
}