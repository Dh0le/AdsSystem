package com.personal.ad.service;

import com.personal.ad.dao.CreativeUnitRepository;
import com.personal.ad.exception.AdException;
import com.personal.ad.vo.*;

public interface IAdUnitService {

    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;
    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException;
    AdUnitItResponse createUnitIt(AdUnitItRequest request)throws AdException;;
    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException;
    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException;

}
