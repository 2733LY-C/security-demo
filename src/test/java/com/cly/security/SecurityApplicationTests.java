package com.cly.security;

import com.cly.security.bean.User;
import com.cly.security.mapper.UserMapper;
import com.cly.security.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SecurityApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() throws Exception {
        //生成JWT
        String jwt = JwtUtil.createJWT("10086","莱欧斯",60*60*1000L);
        System.out.println(jwt);

        //解析JWT
        Claims claims = JwtUtil.parseJWT(jwt);
        System.out.println(claims);
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());

    }

    @Test
    void userMapperTest() {
        List<User> users = userMapper.selectList(null);
        for (User user : users){
            System.out.println(user);}
    }

}
