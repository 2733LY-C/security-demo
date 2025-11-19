package com.cly.security.Controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping
    public String test(){
        return "test";
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('sys:admin')")
    public String hello(){
        return "天王盖地虎";
    }

    @PreAuthorize("hasAuthority('sys:user')")
    @GetMapping("/world")
    public String world(){
        return "宝塔镇河妖";
    }


}
