package com.cly.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.security.bean.User;
import com.cly.security.bean.vo.LoginUser;
import com.cly.security.common.RedisCache;
import com.cly.security.mapper.UserMapper;
import com.cly.security.service.UserService;
import com.cly.security.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
        @Autowired
        private AuthenticationManager authenticationManager;
//        @Resource

        @Autowired
        private RedisCache redisCache;

        @Override
        public String login(User user) {
            //2.封装Authentication对象
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
            //3.调用authentication()进行认证
            Authentication authenticate = authenticationManager.authenticate(authentication);
            // 内部发生以下步骤：
            //   a. 调用您的 UserDetailService.loadUserByUsername(username)
            //   b. 从数据库加载用户信息（包含加密后的密码）
            //   c. 使用 PasswordEncoder 比较用户输入密码和数据库密码
            //   d. 如果匹配，返回认证成功的 Authentication 对象(并检查用户状态、加载用户权限信息)
            if (authenticate == null) {throw new RuntimeException("登录异常");}
            LoginUser principal = (LoginUser) authenticate.getPrincipal();
            //4.将id作为jwt返回，并将用户信息对象存入到redis，并以id为key值，后续可以查询到用户信息
            String jsonString = JSON.toJSONString(principal.getUser().getId());
            //5. 创建jwt令牌返回
            String jwt = JwtUtil.createJWT(jsonString);
            String key= principal.getUser().getId().toString();
            redisCache.setCacheObject(key, principal, JwtUtil.JWT_TTL/1L, TimeUnit.SECONDS);
            return jwt;
        }

}
