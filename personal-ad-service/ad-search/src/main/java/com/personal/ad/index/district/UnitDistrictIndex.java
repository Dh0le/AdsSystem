package com.personal.ad.index.district;

import com.personal.ad.index.IndexAware;
import com.personal.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {

    private static Map<String,Set<Long>> districtUnitMap;
    private static Map<Long,Set<String>> unitDistrictMap;
    static {
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();

    }
    @Override
    public Set<Long> get(String key) {
        return districtUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("unitDistrictIndex before add:{}",unitDistrictMap);
        Set<Long> unitIds = CommonUtils.getOrCreate(key,districtUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(value);
        for(Long id:value){
            Set<String> districts = CommonUtils.getOrCreate(id,unitDistrictMap,ConcurrentSkipListSet::new);
            districts.add(key);
        }
        log.info("unitDistrictIndex after add:{}",unitDistrictMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("District index does not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("unitDistrictIndex before delete:{}",unitDistrictMap);
        Set<Long> unitIds = CommonUtils.getOrCreate(key,districtUnitMap, ConcurrentSkipListSet::new);
        unitIds.removeAll(value);
        for(Long id:value){
            Set<String> districts = CommonUtils.getOrCreate(id,unitDistrictMap,ConcurrentSkipListSet::new);
            districts.remove(key);
        }
        log.info("unitDistrictIndex after delete:{}",unitDistrictMap);
    }
}
