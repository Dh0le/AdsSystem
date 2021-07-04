package com.personal.ad.service;

import com.personal.ad.exception.AdException;
import com.personal.ad.vo.CreateUserRequest;
import com.personal.ad.vo.CreateUserResponse;

public interface IUserService {

    CreateUserResponse createUser(CreateUserRequest request) throws AdException;


}
