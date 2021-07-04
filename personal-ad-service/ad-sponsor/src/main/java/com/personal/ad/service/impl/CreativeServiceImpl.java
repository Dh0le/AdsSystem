package com.personal.ad.service.impl;

import com.personal.ad.constant.Constants;
import com.personal.ad.dao.AdUserRepository;
import com.personal.ad.dao.CreativeRepository;
import com.personal.ad.dao.CreativeUnitRepository;
import com.personal.ad.entity.AdUser;
import com.personal.ad.entity.Creative;
import com.personal.ad.exception.AdException;
import com.personal.ad.service.ICreativeService;
import com.personal.ad.vo.CreativeRequest;
import com.personal.ad.vo.CreativeResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CreativeServiceImpl implements ICreativeService {

    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private AdUserRepository userRepository;
    public CreativeServiceImpl(CreativeRepository creativeRepository, AdUserRepository userRepository) {
        this.creativeRepository = creativeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public CreativeResponse creativeCreative(CreativeRequest request) throws AdException {
        if(request.getUserId() == null || request.getUserId() == 0){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAMETER_ERROR);
        }
        Optional<AdUser> user = userRepository.findById(request.getUserId());
        if(!user.isPresent()){
            throw  new AdException(Constants.ErrorMsg.CANNOT_FIND_RECORD);
        }

        Creative creative = creativeRepository.save(request.convertToEntity());

        return new CreativeResponse(creative.getName(),creative.getId());

    }


}
