package com.personal.ad.service;

import com.personal.ad.entity.AdPlan;
import com.personal.ad.exception.AdException;
import com.personal.ad.vo.AdPlanGetRequest;
import com.personal.ad.vo.AdPlanRequest;
import com.personal.ad.vo.AdPlanResponse;

import java.util.List;

public interface IAdPlanService {
    AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException;
    List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws  AdException;
    AdPlanResponse updateAdPlan(AdPlanRequest request) throws  AdException;
    void deleteAdPlan(AdPlanRequest request) throws  AdException;
}
