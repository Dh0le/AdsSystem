package com.personal.ad.search.vo;

import com.personal.ad.search.vo.feature.DistrictFeature;
import com.personal.ad.search.vo.feature.FeatureRelation;
import com.personal.ad.search.vo.feature.ITFeature;
import com.personal.ad.search.vo.feature.KeywordFeature;
import com.personal.ad.search.vo.media.AdSlot;
import com.personal.ad.search.vo.media.App;
import com.personal.ad.search.vo.media.Device;
import com.personal.ad.search.vo.media.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    //Identification number fo media that send this request.
    private String mediaId;

    private RequestInfo requestInfo;

    private FeatureInfo featureInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestInfo{
        private String requestID;
        private List<AdSlot> adSlots;
        private App app;
        private Geo geo;
        private Device device;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeatureInfo{
        private DistrictFeature districtFeature;
        private ITFeature itFeature;
        private KeywordFeature keywordFeature;
        private FeatureRelation relation = FeatureRelation.AND;
    }
}
