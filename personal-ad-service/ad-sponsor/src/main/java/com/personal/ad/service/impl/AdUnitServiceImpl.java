package com.personal.ad.service.impl;

import com.personal.ad.constant.Constants;
import com.personal.ad.dao.*;
import com.personal.ad.entity.AdPlan;
import com.personal.ad.entity.AdUnit;
import com.personal.ad.entity.CreativeUnit;
import com.personal.ad.entity.unit_condition.AdUnitDistrict;
import com.personal.ad.entity.unit_condition.AdUnitIt;
import com.personal.ad.entity.unit_condition.AdUnitKeyword;
import com.personal.ad.exception.AdException;
import com.personal.ad.service.IAdUnitService;
import com.personal.ad.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private AdPlanRepository planRepository;
    private AdUnitRepository unitRepository;
    private AdUnitKeywordRepository keywordRepository;
    private AdUnitItRepository itRepository;
    private AdUnitDistrictRepository districtRepository;
    private CreativeRepository creativeRepository;
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    public AdUnitServiceImpl(AdPlanRepository planRepository,
                             AdUnitRepository unitRepository,
                             AdUnitKeywordRepository keywordRepository,
                             AdUnitItRepository itRepository,
                             AdUnitDistrictRepository districtRepository,
                             CreativeRepository creativeRepository,
                             CreativeUnitRepository creativeUnitRepository) {
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
        this.keywordRepository = keywordRepository;
        this.itRepository = itRepository;
        this.districtRepository = districtRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }



    @Override
    @Transactional
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if(!request.createValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAMETER_ERROR);
        }
        Optional<AdPlan> adPlan = planRepository.findById(request.getPlanId());
        if(!adPlan.isPresent()){
            throw new AdException(Constants.ErrorMsg.CANNOT_FIND_RECORD);
        }
        AdUnit oldUnit = unitRepository.findByPlanIdAndUnitName(request.getPlanId(),request.getUnitName());
        if(oldUnit != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }
        AdUnit newAdUnit = unitRepository.save(new AdUnit(request.getPlanId(),request.getUnitName(),request.getPositionType(),request.getBudget()));
        return new AdUnitResponse(newAdUnit.getId(), newAdUnit.getUnitName());
    }

    @Override
    @Transactional
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {
        List<Long> unitIds = request.getKeywords().stream().map(AdUnitKeywordRequest.UnitKeyword::getUnitId).collect(Collectors.toList());
        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAMETER_ERROR);
        }

        List<Long> ids = Collections.emptyList();
        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        if(!CollectionUtils.isEmpty(request.getKeywords())){
            request.getKeywords().forEach((i->unitKeywords.add(new AdUnitKeyword(i.getUnitId(),i.getKeyword()))));
        }
        ids = keywordRepository.saveAll(unitKeywords).stream().map(AdUnitKeyword::getUnitId).collect(Collectors.toList());
        return new AdUnitKeywordResponse(ids);
    }

    @Override
    @Transactional
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIds = request.getUnitIts().stream().map(AdUnitItRequest.UnitIt::getUnitId).collect(Collectors.toList());
        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAMETER_ERROR);
        }
        List<AdUnitIt> unitIts = new ArrayList<>();
        request.getUnitIts().forEach(i->unitIts.add(new AdUnitIt(i.getUnitId(),i.getItTag())));
        List<Long> ids = itRepository.saveAll(unitIts).stream().map(AdUnitIt::getId).collect(Collectors.toList());

        return null;
    }

    @Override
    @Transactional
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIds = request.getDistricts().stream().map(AdUnitDistrictRequest.UnitDistrict::getUnitId).collect(Collectors.toList());
        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAMETER_ERROR);
        }
        List<AdUnitDistrict> AdUnitDistricts= new ArrayList<>();
        request.getDistricts().forEach(i->AdUnitDistricts.add(new AdUnitDistrict(i.getUnitId(),i.getProvince(),i.getCity())));
        List<Long> ids = districtRepository.saveAll(AdUnitDistricts).stream().map(AdUnitDistrict::getId).collect(Collectors.toList());
        return new AdUnitDistrictResponse(ids);
    }

    @Override
    @Transactional
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {
        List<Long> creativeIds = request.getUnitItems().stream().map(CreativeUnitRequest.CreativeUnitItem::getCreativeId).collect(Collectors.toList());
        List<Long> unitIds = request.getUnitItems().stream().map(CreativeUnitRequest.CreativeUnitItem::getUnitId).collect(Collectors.toList());
        if(!isRelatedUnitExist(unitIds) || !isRelatedCreativeExist(creativeIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAMETER_ERROR);
        }
        List<CreativeUnit> creativeUnits= new ArrayList<>();
        request.getUnitItems().forEach(i->creativeUnits.add(new CreativeUnit(i.getCreativeId(),i.getUnitId())));
        List<Long> ids = creativeUnitRepository.saveAll(creativeUnits).stream().map(CreativeUnit::getId).collect(Collectors.toList());

        return new CreativeUnitResponse(ids);

    }


    private boolean isRelatedUnitExist(List<Long> unitIds){
        if(CollectionUtils.isEmpty(unitIds)){
            return false;
        }
        return unitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }

    private boolean isRelatedCreativeExist(List<Long> creativeIds){
        if(CollectionUtils.isEmpty(creativeIds)){
            return false;
        }
        return creativeRepository.findAllById(creativeIds).size() == new HashSet<>(creativeIds).size();
    }
}
