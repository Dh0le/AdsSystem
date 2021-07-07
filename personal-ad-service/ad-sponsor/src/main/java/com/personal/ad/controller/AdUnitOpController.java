package com.personal.ad.controller;

import com.alibaba.fastjson.JSON;
import com.personal.ad.exception.AdException;
import com.personal.ad.service.IAdUnitService;
import com.personal.ad.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AdUnitOpController {

    private IAdUnitService unitService;

    @Autowired
    public AdUnitOpController(IAdUnitService unitService) {
        this.unitService = unitService;
    }

    @PostMapping("/create/adUnit")
    public AdUnitResponse createUnit(@RequestBody AdUnitRequest request) throws AdException{
        log.info("ad_sponsor:createUnit->{}", JSON.toJSONString(request));
        return unitService.createUnit(request);
    }

    @PostMapping("/create/adUnitKeyword")
    public AdUnitKeywordResponse createUnitKeyword(@RequestBody AdUnitKeywordRequest request) throws  AdException{
        log.info("ad_sponsor:createUnitKeyword->{}",JSON.toJSONString(request));
        return unitService.createUnitKeyword(request);
    }

    @PostMapping("/create/adUnitIt")
    public AdUnitItResponse createUnitIt(@RequestBody AdUnitItRequest request) throws AdException{
        log.info("ad-sponsor:createUnitIt->{}",JSON.toJSONString(request));
        return unitService.createUnitIt(request);
    }

    @PostMapping("/create/adUnitDistrict")
    public AdUnitDistrictResponse createUnitDistrict(@RequestBody AdUnitDistrictRequest request) throws AdException{
        log.info("ad-sponsor:createUnitDistrict->{}",JSON.toJSONString(request));
        return unitService.createUnitDistrict(request);
    }


    @PostMapping("/create/creativeUnit")
    public CreativeUnitResponse createCreativeUnit(@RequestBody CreativeUnitRequest request) throws AdException{
        log.info("ad-sponsor:createCreativeUnit->{}",JSON.toJSONString(request));
        return unitService.createCreativeUnit(request);
    }
}
