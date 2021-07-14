package com.personal.ad.index.keyword;

import com.personal.ad.index.IndexAware;
import com.personal.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {
    private static Map<String,Set<Long>> keyWordUnitMap;
    private static Map<Long,Set<String>> unitKeywordMap;

    static{
        keyWordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        if(StringUtils.isEmpty(key)){
            return Collections.emptySet();
        }
        Set<Long> result = keyWordUnitMap.get(key);
        if(result == null){
            return Collections.emptySet();
        }return result;
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitKeywordIndex, before add:{}",unitKeywordMap);
        Set<Long> unitIdSet = CommonUtils.getOrCreate(key,keyWordUnitMap,ConcurrentSkipListSet::new);
        unitIdSet.addAll(value);
        for(Long unitId:value){
            Set<String> keywordSet = CommonUtils.getOrCreate(unitId,unitKeywordMap,ConcurrentSkipListSet::new);
            keywordSet.add(key);
        }
        log.info("UnitKeywordIndex, after add:{}",unitKeywordMap);

    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("Keyword index does not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitedKeywordIndex, before delete:{}",unitKeywordMap);
        Set<Long> unitIds = CommonUtils.getOrCreate(key,keyWordUnitMap,ConcurrentSkipListSet::new);
        unitIds.removeAll(value);
        for(Long unitId:value){
            Set<String> keywordSet = CommonUtils.getOrCreate(unitId,unitKeywordMap,ConcurrentSkipListSet::new);
            keywordSet.remove(key);
        }
        log.info("UnitedKeywordIndex, after delete:{}",unitKeywordMap);
    }

    public boolean match(Long unitId, List<String> keywords){
        if(unitKeywordMap.containsKey(unitId) && !CollectionUtils.isEmpty(unitKeywordMap.get(unitId))){
            Set<String> unitKeyword = unitKeywordMap.get(unitId);
            return CollectionUtils.isSubCollection(keywords,unitKeyword);
        }
        return false;
    }
}
