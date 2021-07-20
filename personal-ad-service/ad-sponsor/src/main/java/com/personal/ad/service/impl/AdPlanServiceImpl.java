package com.personal.ad.service.impl;

import com.personal.ad.constant.CommonStatus;
import com.personal.ad.constant.Constants;
import com.personal.ad.dao.AdPlanRepository;
import com.personal.ad.dao.AdUserRepository;
import com.personal.ad.entity.AdPlan;
import com.personal.ad.entity.AdUser;
import com.personal.ad.exception.AdException;
import com.personal.ad.service.IAdPlanService;
import com.personal.ad.utils.CommonUtils;
import com.personal.ad.vo.AdPlanGetRequest;
import com.personal.ad.vo.AdPlanRequest;
import com.personal.ad.vo.AdPlanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdPlanServiceImpl  implements IAdPlanService {
    private AdUserRepository userRepository;
    private AdPlanRepository planRepository;

    @Autowired
    public AdPlanServiceImpl(AdUserRepository userRepository, AdPlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Override
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException {
        if(!request.createValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAMETER_ERROR);
        }
        Optional<AdUser> adUser = userRepository.findById(request.getUserId());
        if(!adUser.isPresent()){
            throw new AdException(Constants.ErrorMsg.CANNOT_FIND_RECORD);
        }
        AdPlan oldAdPlan = planRepository.findByUserIdAndPlanName(request.getUserId(),request.getPlanName());
        if(oldAdPlan != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_PLAN_ERROR);
        }

        AdPlan newAdPlan = planRepository.save(
                new AdPlan(request.getUserId(),
                request.getPlanName(),
                CommonUtils.parseStringDate(request.getStartDate()),
                CommonUtils.parseStringDate(request.getEndDate())));
        return new AdPlanResponse(newAdPlan.getId(),newAdPlan.getPlanName());
    }

    @Override
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {
        if(!request.validate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAMETER_ERROR);
        }
        return planRepository.findAllByIdAndUserId(request.getIds(), request.getUserId());
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException {
        if(!request.updateValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAMETER_ERROR);
        }
        AdPlan plan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if(plan == null){
            throw  new AdException(Constants.ErrorMsg.CANNOT_FIND_RECORD);
        }
        if(request.getPlanName() != null){
            plan.setPlanName(request.getPlanName());
        }
        if(request.getStartDate() != null){
            plan.setStartDate(CommonUtils.parseStringDate(request.getStartDate()));
        }
        if(request.getEndDate() != null){
            plan.setEndDate(CommonUtils.parseStringDate(request.getEndDate()));
        }

        plan.setUpdateTime(new Date());

        plan = planRepository.save(plan);

        return new AdPlanResponse(plan.getId(),plan.getPlanName());
    }

    @Override
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws AdException {
        if(!request.deleteValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAMETER_ERROR);
        }
        AdPlan plan = planRepository.findByUserIdAndPlanName(request.getId(),request.getPlanName());
        if(plan == null){
            throw new AdException(Constants.ErrorMsg.CANNOT_FIND_RECORD);
        }

        plan.setPlanStatus(CommonStatus.INVALID.getStatus());
        plan.setUpdateTime(new Date());
        planRepository.save(plan);
    }
}
