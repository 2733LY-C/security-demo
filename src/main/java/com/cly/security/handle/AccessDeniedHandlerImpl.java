package com.cly.security.handle;


import com.alibaba.fastjson.JSON;
import com.cly.security.common.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 权限未授予处理器
 *
 * 在 SpringSecurity 中，如果我们在认证或者授权的过程中出现了异常会被 ExceptionTranslationFilter 捕获到。
 *
 * 如果是认证过程中出现的异常会被封装成 AuthenticationException 然后调用 AuthenticationEntryPoint 对象的方法去进行异常处理
 * 如果是授权过程中出现的异常会被封装成 AccessDeniedException 然后调用 AccessDeniedHandler 对象的方法去进行异常处理
 * ————————————————
 * 版权声明：本文为CSDN博主「残花月伴」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/weixin_68576312/article/details/153456798
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        R<Object> fail = R.fail(403, "权限不足");
        String json = JSON.toJSONString(fail);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
