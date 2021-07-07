package com.personal.ad.controller;

import com.alibaba.fastjson.JSON;
import com.personal.ad.exception.AdException;
import com.personal.ad.service.IUserService;
import com.personal.ad.vo.CreateUserRequest;
import com.personal.ad.vo.CreateUserResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserOpController {

    @Autowired
    private IUserService userService;

    public UserOpController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create/user")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) throws AdException {
        log.info("ad-sponsor:CreateUser - >{}", JSON.toJSONString(request));
        return userService.createUser(request);
    }
}
