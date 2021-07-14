package com.personal.ad.controller;

import com.alibaba.fastjson.JSON;
import com.personal.ad.annotation.IgnoreResponseAdvice;
import com.personal.ad.client.SponsorClient;
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

    private SponsorClient client;

    @Autowired
    public SearchController(SponsorClient client) {
        this.client = client;
    }

    @IgnoreResponseAdvice
    @PostMapping("/getAdPlans")
    public CommonResponse<List<AdPlan>> getAdPlans(
            @RequestBody AdPlanGetRequest request
    ){
        log.info("ad-search:getAdPlans->{}",JSON.toJSONString(request));
        return client.getAdPlans(request);
    }
}
