package com.cly.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cly.security.bean.User;
import com.cly.security.bean.vo.LoginUser;
import com.cly.security.mapper.UserMapper;
import com.cly.security.service.UserService;
import com.cly.security.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String login(User user) {
        //不需要连接数据库
        //把登录时候的用户名和密码封装成UsernamePasswordAuthenticationToken 对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //通过AuthenticationManager的authenticate 方法来进行用户认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //3.如果认证不通过，就返回自定义的异常
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }
        //如果认证成功，就从authenticate对象的getPrincipal方法中拿到认证通过后的登录用户对象
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        //生成jwt ，使用fastjson的方法，把对象转换成字符串
        String loginUserString = JSON.toJSONString(loginUser);
        //调用jwt 工具类，生成令牌
        String jwt = JwtUtil.createJWT(loginUserString,null);
        return jwt;

    }
}
