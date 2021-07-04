package com.personal.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitKeywordRequest {

    private List<UnitKeyword> keywords;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitKeyword{
        private Long unitId;
        private String keyword;
    }
}
