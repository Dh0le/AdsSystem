package com.personal.ad.index.adunit;

import com.personal.ad.index.adplan.AdPlanObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitObject {
    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;

    private AdPlanObject adPlanObject;

    void update(AdUnitObject newObject){
        if(newObject.getUnitId() != null){
            this.unitId = newObject.getUnitId();
        }
        if(newObject.getUnitStatus() != null){
            this.unitStatus = newObject.getUnitStatus();
        }
        if(newObject.getPositionType() != null){
            this.positionType = newObject.getPositionType();
        }
        if(newObject.getPlanId() != null){
            this.planId = newObject.getPlanId();
        }
        if(newObject.getAdPlanObject() != null){
            this.adPlanObject = newObject.getAdPlanObject();
        }
    }
    private static boolean isOpenScreen(int positionType){
        return (positionType & AdUnitConstants.POSITION_TYPE.openScreen) > 0;
    }
    private static boolean isPreMovie(int positionType){
        return (positionType & AdUnitConstants.POSITION_TYPE.preMovie) > 0;
    }
    private static boolean isInMovie(int positionType){
        return (positionType & AdUnitConstants.POSITION_TYPE.inMovie) > 0;
    }
    private static boolean isPostMovie(int positionType){
        return (positionType & AdUnitConstants.POSITION_TYPE.postMovie) > 0;
    }
    private static boolean isPauseMovie(int positionType){
        return (positionType & AdUnitConstants.POSITION_TYPE.pauseMovie) > 0;
    }
    public static boolean isAdSlotTypeOK(int adSlotType, int positionType){
        switch (adSlotType) {
            case AdUnitConstants.POSITION_TYPE.openScreen:
                return isOpenScreen(positionType);
            case AdUnitConstants.POSITION_TYPE.preMovie:
                return isPreMovie(positionType);
            case AdUnitConstants.POSITION_TYPE.inMovie:
                return isInMovie(positionType);
            case AdUnitConstants.POSITION_TYPE.pauseMovie:
                return isPauseMovie(positionType);
            case AdUnitConstants.POSITION_TYPE.postMovie:
                return isPostMovie(positionType);
            default:
                return false;
        }
    }

}
