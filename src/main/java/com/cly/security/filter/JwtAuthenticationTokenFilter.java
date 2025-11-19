package com.cly.security.filter;

import com.alibaba.fastjson.JSON;
import com.cly.security.bean.vo.LoginUser;
import com.cly.security.common.R;
import com.cly.security.common.RedisCache;
import com.cly.security.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//每一个servlet请求，只会请求一次
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private  RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        //判断如果是登录接口，直接放行  不走UsernameAndPassword
        if (uri.equals("/user/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        //判断token 是否为空
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("登录异常");
        }
        // 校验令牌

        try {
            Claims claims = JwtUtil.parseJWT(token);
            String subject = claims.getSubject();
            //把字符串转换成LoginUser 对象
            String userId = JSON.parseObject(subject, String.class);

            LoginUser loginUser =  redisCache.getCacheObject(userId);

            if (loginUser != null) {
            //获取权限信息，封装到authentication中
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
            //将验证完的信息放进security上下文
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(R.fail(401, "token错误")));
        }

        filterChain.doFilter(request, response);

    }
}
