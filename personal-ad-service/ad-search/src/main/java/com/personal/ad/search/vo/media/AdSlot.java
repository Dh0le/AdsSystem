package com.personal.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdSlot {
    //code foe ads position;
    private String adSlotCode;

//  type of the ads
    private int positionType;

    //size of the ads
    private int height;
    private int width;

    //type of the media
    private List<Integer> type;
    //lowest bit price
    private Integer minCpm;
}
