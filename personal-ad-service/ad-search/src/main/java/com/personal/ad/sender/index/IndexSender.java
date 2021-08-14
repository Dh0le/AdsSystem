package com.personal.ad.sender.index;

import com.alibaba.fastjson.JSON;
import com.personal.ad.dump.table.*;
import com.personal.ad.handler.AdLevelDataHandler;
import com.personal.ad.index.DataLevel;
import com.personal.ad.mysql.constant.Constant;
import com.personal.ad.mysql.dto.MySqlRowData;
import com.personal.ad.sender.ISender;
import com.personal.ad.utils.CommonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("indexSender")
public class IndexSender implements ISender {
    @Override
    public void sender(MySqlRowData rowData) {
        String level = rowData.getLevel();
        if(DataLevel.LEVEL2.getLevel().equals(level)){
            Level2RowData(rowData);
        }else if(DataLevel.LEVEL3.getLevel().equals(level)){
            Level3RowData(rowData);
        }else if(DataLevel.LEVEL4.getLevel().equals(level)){
            Level4RowData(rowData);
        }else{
            log.error("MysqlRowData ERROR:{}", JSON.toJSONString(rowData));
        }
    }

    private void Level2RowData(MySqlRowData rowData){
        if(rowData.getTableName().equals(Constant.AD_PLAN_TABLE_INFO.TABLE_NAME)){
            List<AdPlanTable> planTables = new ArrayList<>();
            for (Map<String,String> fieldValueMap:rowData.getFieldValueMap()
                 ) {
                AdPlanTable planTable = new AdPlanTable();
                fieldValueMap.forEach((k,v)->{
                    switch (k){
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            planTable.setId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            planTable.setUserID(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            planTable.setPlanStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            planTable.setStartDate(CommonUtils.parseStringDate(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_END_DATE:
                            planTable.setEndDate(CommonUtils.parseStringDate(v));
                            break;

                    }
                });
                planTables.add(planTable);
            }
            planTables.forEach(p-> AdLevelDataHandler.handleLevel2(p,rowData.getOpType()));
        }else if(rowData.getTableName().equals(Constant.AD_CREATIVE_TABLE_INFO.TABLE_NAME)){
            List<AdCreativeTable> creativeTables = new ArrayList<>();
            for(Map<String,String> fieldValMap:rowData.getFieldValueMap()){
                AdCreativeTable creativeTable = new AdCreativeTable();
                fieldValMap.forEach((k,v)->{
                    switch (k){
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                            creativeTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_TYPE:
                            creativeTable.setType(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_HEIGHT:
                            creativeTable.setHeight(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_WIDTH:
                            creativeTable.setWidth(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                            creativeTable.setAuditStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_URL:
                            creativeTable.setAdUrl(v);
                            break;
                    }
                });
                creativeTables.add(creativeTable);
            }
            creativeTables.forEach(c->AdLevelDataHandler.handleLevel2(c,rowData.getOpType()));
        }
    }

    private void Level3RowData(MySqlRowData rowData){
        if(rowData.getTableName().equals(Constant.AD_UNIT_TABLE_INFO.TABLE_NAME)){
            List<AdUnitTable> unitTables = new ArrayList<>();
            for(Map<String,String> fieldValueMap : rowData.getFieldValueMap()){
                AdUnitTable table = new AdUnitTable();
                fieldValueMap.forEach((k,v)->{
                    switch (k){
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            table.setUnitId(Long.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            table.setUnitStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            table.setPositionType(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            table.setPlanId(Long.valueOf(v));
                            break;
                    }
                });
                unitTables.add(table);
            }
            unitTables.forEach(u->AdLevelDataHandler.handleLevel3(u,rowData.getOpType()));
        }
        else if(rowData.getTableName().equals(Constant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME)){
            List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
            for(Map<String,String> fieldValueMap:rowData.getFieldValueMap()){
                AdCreativeUnitTable table = new AdCreativeUnitTable();
                fieldValueMap.forEach((k,v)->{
                    switch (k){
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            table.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            table.setUnitId(Long.valueOf(v));
                            break;
                    }
                });
                creativeUnitTables.add(table);
            }
            creativeUnitTables.forEach(c->AdLevelDataHandler.handleLevel3(c,rowData.getOpType()));
        }
    }
    private void Level4RowData(MySqlRowData rowData){
        switch (rowData.getTableName()){
            case Constant.AD_UNIT_DISTRICT_INFO.TABLE_NAME:
                List<AdUnitDistrictTable> districtTables = new ArrayList<>();
                for(Map<String,String> fieldValueMap: rowData.getFieldValueMap()){
                    AdUnitDistrictTable table = new AdUnitDistrictTable();
                    fieldValueMap.forEach((k,v)->{
                        switch (k){
                            case Constant.AD_UNIT_DISTRICT_INFO.COLUMN_UNIT_ID:
                                table.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_DISTRICT_INFO.COLUMN_PROVINCE:
                                table.setProvince(v);
                                break;
                            case Constant.AD_UNIT_DISTRICT_INFO.COLUMN_CITY:
                                table.setCity(v);
                                break;
                        }
                    });
                    districtTables.add(table);
                }
                districtTables.forEach(t->{
                    AdLevelDataHandler.handlerLevel4(t,rowData.getOpType());
                });
                break;
            case Constant.AD_UNIT_IT_INFO.TABLE_NAME:
                List<AdUnitItTable> itTables = new ArrayList<>();
                for(Map<String,String> fieldValueMap: rowData.getFieldValueMap()){
                    AdUnitItTable itTable = new AdUnitItTable();
                    fieldValueMap.forEach((k,v)->{
                        switch (k){
                            case Constant.AD_UNIT_IT_INFO.COLUMN_UNIT_ID:
                                itTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_IT_INFO.COLUMN_IT_TAG:
                                itTable.setItTag(v);
                                break;
                        }
                    });
                    itTables.add(itTable);
                }
                itTables.forEach(i->{
                    AdLevelDataHandler.handlerLevel4(i,rowData.getOpType());
                });
                break;
            case Constant.AD_UNIT_KEYWORD_INFO.TABLE_NAME:
                List<AdUnitKeywordTable> keywordTables = new ArrayList<>();
                for(Map<String,String> fieldValueMap:rowData.getFieldValueMap()){
                    AdUnitKeywordTable keywordTable = new AdUnitKeywordTable();
                    fieldValueMap.forEach((k,v)->{
                        switch (k){
                            case Constant.AD_UNIT_KEYWORD_INFO.COLUMN_UNIT_ID:
                                keywordTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_KEYWORD_INFO.COLUMN_KEYWORD:
                                keywordTable.setKeyword(v);
                                break;
                        }
                    });
                    keywordTables.add(keywordTable);
                }
                keywordTables.forEach(k->{
                    AdLevelDataHandler.handlerLevel4(k,rowData.getOpType());
                });
                break;
        }
    }
}
