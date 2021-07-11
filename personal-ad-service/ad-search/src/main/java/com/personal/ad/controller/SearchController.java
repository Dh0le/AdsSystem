package com.personal.ad.controller;

import com.alibaba.fastjson.JSON;
import com.personal.ad.annotation.IgnoreResponseAdvice;
import com.personal.ad.client.vo.AdPlan;
import com.personal.ad.client.vo.AdPlanGetRequest;
import com.personal.ad.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class SearchController {

    private RestTemplate restTemplate;

    @Autowired
    public SearchController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("all")
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlansByRibbon")
    public CommonResponse<List<AdPlan>> getAdPlansByRibbon(
            @RequestBody AdPlanGetRequest request
            ){
        log.info("ad-search:getAdPlanByRibbon->{}", JSON.toJSONString(request));
        return restTemplate.postForEntity(
                "http://eureka-client-ad-sponsor/ad-sponsor/get/adPlan",request,CommonResponse.class
        ).getBody();
    }
}
