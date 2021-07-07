package com.personal.ad.controller;

import com.alibaba.fastjson.JSON;
import com.personal.ad.entity.AdPlan;
import com.personal.ad.exception.AdException;
import com.personal.ad.service.IAdPlanService;
import com.personal.ad.vo.AdPlanGetRequest;
import com.personal.ad.vo.AdPlanRequest;
import com.personal.ad.vo.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class AdPlanOpController {

    @Autowired
    private final IAdPlanService planService;

    public AdPlanOpController(IAdPlanService planService) {
        this.planService = planService;
    }

    @PostMapping("/create/adPlan")
    public AdPlanResponse createAdPlan(@RequestBody AdPlanRequest request)throws AdException {
        log.info("ad-sponsor: CreateAdPlan->{}", JSON.toJSONString(request));
        return planService.createAdPlan(request);
    }

    @PostMapping("/get/adPlan")
    public List<AdPlan> getAdPlanByIds(@RequestBody AdPlanGetRequest request)throws  AdException{
        log.info("ad-sponsor:getAdPlanByIds->{}",JSON.toJSONString(request));
        return planService.getAdPlanByIds(request);
    }

    @PostMapping("/update/adPlan")
    public AdPlanResponse updateAdPlan(@RequestBody AdPlanRequest request) throws  AdException{
        log.info("ad-sponsor:updateAdPlan->{}",JSON.toJSONString(request));
        return planService.updateAdPlan(request);
    }

    @PostMapping("/delete/adPlan")
    public void deleteAdPlan(@RequestBody AdPlanRequest request)throws AdException{
        log.info("ad-sponsor:deleteAdPlan->{}",JSON.toJSONString(request));
        planService.deleteAdPlan(request);
    }

}
