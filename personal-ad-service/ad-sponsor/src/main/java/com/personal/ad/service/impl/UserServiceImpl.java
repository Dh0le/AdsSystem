package com.personal.ad.service.impl;

import com.personal.ad.constant.Constants;
import com.personal.ad.dao.AdUserRepository;
import com.personal.ad.entity.AdUser;
import com.personal.ad.exception.AdException;
import com.personal.ad.vo.CreateUserRequest;
import com.personal.ad.vo.CreateUserResponse;
import com.personal.ad.service.IUserService;
import com.personal.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private AdUserRepository userRepository;

    @Autowired
    public UserServiceImpl(AdUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) throws AdException {
        if(!request.validate()){
            throw  new AdException(Constants.ErrorMsg.REQUEST_PARAMETER_ERROR);
        }

        AdUser oldUser = userRepository.findByUsername(request.getUserName());

        if(oldUser != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }

        AdUser newUser =  userRepository.save(
                new AdUser(request.getUserName(),
                CommonUtils.md5(request.getUserName())));

        return new CreateUserResponse(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getToken(),
                newUser.getCreateTime(),
                newUser.getUpdateTime()
        );
    }
}
