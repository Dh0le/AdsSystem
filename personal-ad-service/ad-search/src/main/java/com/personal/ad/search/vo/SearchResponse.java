package com.personal.ad.search.vo;

import com.personal.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    Map<String,List<Creative>> adSlot2Ads = new HashMap<>();
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Creative{
        private Long adId;
        private String url;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;

        //url for monitor and show
        //after media show ads to audience
        private List<String> showMonitorUrl = Arrays.asList("www.imooc.com","www.imooc.com");
        // url for click
        // after user click the ads
        private List<String> clickMonitorUrl = Arrays.asList("www.imooc.com","www.imooc.com");
    }
    public static Creative convert(CreativeObject object){
        Creative creative = new Creative();
        creative.setAdId(object.getAdId());
        creative.setUrl(object.getAdUrl());
        creative.setWidth(object.getWidth());
        creative.setHeight(object.getHeight());
        creative.setType(object.getType());
        creative.setMaterialType(object.getMaterialType());
        return creative;
    }
}
