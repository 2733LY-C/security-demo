package com.cly.security.Controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cly.security.bean.User;
import com.cly.security.bean.vo.LoginUser;
import com.cly.security.common.R;
import com.cly.security.common.RedisCache;
import com.cly.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisCache redisCache;

    @PostMapping("/login")
    public R login(@RequestBody User user){
        String jwt = userService.login(user);
        if (!StringUtils.isBlank(jwt)){
            return R.ok(jwt,"登录成功");
        }
        return R.fail();
    }

    @GetMapping("/loginOut")
    public R loginOut(HttpServletRequest  request, HttpServletResponse  response){
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }
        //获取security 上下文中的用户信息（获取当前线程的登录用户信息，返回的对象是Authentication对象）
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            //清除security上下文
            new SecurityContextLogoutHandler().logout(request, response, authentication);

            //清除redis中的token
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            String redisKey = loginUser.getUser().getId().toString();
            redisCache.deleteObject(redisKey);
        }

        return R.ok();
    }


}
