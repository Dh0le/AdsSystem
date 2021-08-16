package com.personal.ad.search.impl;

import com.alibaba.fastjson.parser.Feature;
import com.personal.ad.index.DataTable;
import com.personal.ad.index.adunit.AdUnitIndex;
import com.personal.ad.index.district.UnitDistrictIndex;
import com.personal.ad.index.interest.UnitItIndex;
import com.personal.ad.index.keyword.UnitKeywordIndex;
import com.personal.ad.search.ISearch;
import com.personal.ad.search.vo.SearchRequest;
import com.personal.ad.search.vo.SearchResponse;
import com.personal.ad.search.vo.feature.DistrictFeature;
import com.personal.ad.search.vo.feature.FeatureRelation;
import com.personal.ad.search.vo.feature.ITFeature;
import com.personal.ad.search.vo.feature.KeywordFeature;
import com.personal.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AdvisedSupportListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Slf4j
@Service
public class SearchImpl implements ISearch {
    @Override
    public SearchResponse fetchAds(SearchRequest request) {
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();

        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        ITFeature itFeature = request.getFeatureInfo().getItFeature();

        FeatureRelation relation = request.getFeatureInfo().getRelation();

        SearchResponse response = new SearchResponse();
        Map<String,List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();
        for(AdSlot slot:adSlots){

            Set<Long> targetUnitIdSet;
            // prefilter the adUnit ids according to different type of media;
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(slot.getPositionType());
            if(relation == FeatureRelation.AND){
                filterKeywordFeature(adUnitIdSet,keywordFeature);
                filterDistrictFeature(adUnitIdSet,districtFeature);
                filterItFeature(adUnitIdSet,itFeature);
                targetUnitIdSet = adUnitIdSet;
            }else{

                targetUnitIdSet = getOrRelation(adUnitIdSet,keywordFeature,districtFeature,itFeature);
            }
        }
        return null;
    }
    private void filterKeywordFeature(Collection<Long> adUnitIds, KeywordFeature feature){
        if(CollectionUtils.isEmpty(adUnitIds))return;
        if(CollectionUtils.isNotEmpty(feature.getKeywords())){
            CollectionUtils.filter(adUnitIds,adUnitId->DataTable.of(UnitKeywordIndex.class).match(adUnitId, feature.getKeywords()));
        }
    }
    private void filterDistrictFeature(Collection<Long> adUnitIds,DistrictFeature feature){
        if(CollectionUtils.isEmpty(adUnitIds))return;
        if(CollectionUtils.isNotEmpty(feature.getDistricts())){
            CollectionUtils.filter(adUnitIds,adUnitId->DataTable.of(UnitDistrictIndex.class).match(adUnitId, feature.getDistricts()));
        }
    }
    private void filterItFeature(Collection<Long> adUnitIds,ITFeature feature){
        if(CollectionUtils.isEmpty(adUnitIds))return;
        if(CollectionUtils.isNotEmpty(feature.getIts())) {
            CollectionUtils.filter(adUnitIds, adUnitId -> DataTable.of(UnitItIndex.class).match(adUnitId, feature.getIts()));
        }
    }
    private Set<Long> getOrRelation(Set<Long> adUnitIdSet,
                                    KeywordFeature keywordFeature,
                                    DistrictFeature districtFeature,
                                    ITFeature itFeature){
        if(CollectionUtils.isEmpty(adUnitIdSet)){
            return Collections.emptySet();
        }
        Set<Long> keywordIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> itIdSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordIdSet,keywordFeature);
        filterDistrictFeature(districtIdSet,districtFeature);
        filterItFeature(itIdSet,itFeature);

        return new HashSet<>(CollectionUtils.union(keywordIdSet,CollectionUtils.union(keywordIdSet,districtIdSet)));



    }
}
