package com.cly.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cly.security.bean.User;

public interface UserService extends IService<User> {
    String login(User user);
}
