package com.cly.security.Controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cly.security.bean.User;
import com.cly.security.common.R;
import com.cly.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody User user){
        String jwt = userService.login(user);
        if (!StringUtils.isBlank(jwt)){
            return R.ok(jwt,"登录成功");
        }
        return R.fail();
    }


}
