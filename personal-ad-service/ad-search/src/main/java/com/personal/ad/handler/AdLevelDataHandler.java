package com.personal.ad.handler;

import com.personal.ad.dump.table.*;
import com.personal.ad.index.DataTable;
import com.personal.ad.index.IndexAware;
import com.personal.ad.index.adplan.AdPlanIndex;
import com.personal.ad.index.adplan.AdPlanObject;
import com.personal.ad.index.adunit.AdUnitIndex;
import com.personal.ad.index.adunit.AdUnitObject;
import com.personal.ad.index.creative.CreativeIndex;
import com.personal.ad.index.creative.CreativeObject;
import com.personal.ad.index.creativeunit.CreativeUnitIndex;
import com.personal.ad.index.creativeunit.CreativeUnitObject;
import com.personal.ad.index.district.UnitDistrictIndex;
import com.personal.ad.index.interest.UnitItIndex;
import com.personal.ad.index.keyword.UnitKeywordIndex;
import com.personal.ad.mysql.constant.OpType;
import com.personal.ad.utils.CommonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class AdLevelDataHandler {
    public static void handleLevel2(AdPlanTable planTable, OpType type){
        AdPlanObject planObject = new AdPlanObject(
                planTable.getId(),
                planTable.getUserID(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate()
        );
        handleBinlogEvent(DataTable.of(AdPlanIndex.class),planObject.getPlanId(),planObject,type);
    }

    public static void handleLevel2(AdCreativeTable creativeTable,OpType type){
        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl()
        );
        handleBinlogEvent(DataTable.of(CreativeIndex.class),creativeObject.getAdId(),creativeObject,type);
    }

    public static void handleLevel3(AdUnitTable unitTable, OpType type){
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        if( adPlanObject == null){
            log.error("handleLevel3 found AdPlanObject error{}",unitTable.getPlanId());
            return;
        }
        AdUnitObject unitObject = new AdUnitObject(
                unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlanId(),
                adPlanObject
        );
        handleBinlogEvent(DataTable.of(AdUnitIndex.class),unitTable.getUnitId(),unitObject,type);
    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable,OpType type){
        if(type == OpType.UPDATE){
            log.error("CreativeUnit index does not support update");
            return;
        }

        AdUnitObject adUnitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        if(adUnitObject == null ){
            log.error("handleLevel3 adUnit does not exist:{}",creativeUnitTable.getUnitId());
            return;
        }
        if(creativeObject == null){
            log.error("handleLevel3 creative does not exist:{}",creativeUnitTable.getAdId());
            return;
        }
        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getAdId(),
                creativeUnitTable.getUnitId()
        );

        handleBinlogEvent(DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(
                        creativeUnitObject.getAdId().toString(),
                        creativeUnitObject.getUnitId().toString()
                ),
                creativeUnitObject,type
                );
    }

    public static void handlerLevel4(AdUnitDistrictTable unitDistrictTable,OpType type){
        if(type == OpType.UPDATE){
            log.error("unitDistrictTable does not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if(unitObject == null){
            log.error("Current unit dose not exist:{}",unitDistrictTable.getUnitId());
            return;
        }
        String key = CommonUtils.stringConcat(unitDistrictTable.getProvince(),unitDistrictTable.getCity());
        Set<Long> value = new HashSet<>(Collections.singleton(unitDistrictTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitDistrictIndex.class),key,value,type);
    }

    public static void handlerLevel4(AdUnitKeywordTable keywordTable, OpType type){
        if(type == OpType.UPDATE){
            log.error("unitKeywordTable does not support update:{}",keywordTable.getUnitId());
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(keywordTable.getUnitId());
        if(unitObject == null){
            log.error("Current unit dose not exist:{}",keywordTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(keywordTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitKeywordIndex.class),keywordTable.getKeyword(),value,type);

    }

    public static void handlerLevel4(AdUnitItTable itTable, OpType type){
        if(type == OpType.UPDATE){
            log.error("unitKeywordTable does not support update:{}",itTable.getUnitId());
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(itTable.getUnitId());
        if(unitObject == null){
            log.error("Current unit dose not exist:{}",itTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(itTable.getUnitId()));
        handleBinlogEvent(
                DataTable.of(UnitItIndex.class),itTable.getItTag(),value,type
        );

    }

    private static <K,V>  void handleBinlogEvent(IndexAware<K,V> index, K key, V value, OpType type){
        switch (type){
            case ADD:
                index.add(key,value);
                break;
            case UPDATE:
                index.update(key,value);
                break;
            case DELETE:
                index.delete(key,value);
            default:
                break;
        }
    }
}
