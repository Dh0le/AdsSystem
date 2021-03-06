package com.personal.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictFeature {
    List<ProvinceAndCity> districts;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProvinceAndCity{
        private String province;
        private String city;
    }
}
