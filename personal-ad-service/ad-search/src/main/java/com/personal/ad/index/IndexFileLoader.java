package com.personal.ad.index;

import com.alibaba.fastjson.JSON;
import com.personal.ad.dump.DConstant;
import com.personal.ad.dump.table.*;
import com.personal.ad.handler.AdLevelDataHandler;
import com.personal.ad.index.creativeunit.CreativeUnitObject;
import com.personal.ad.mysql.constant.OpType;
import com.sun.javafx.iio.ios.IosDescriptor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@DependsOn("dataTable")
public class IndexFileLoader {
    @PostConstruct
    private void init(){
        //Load level 2 index from file
        List<String> adPlanStrings = loadDumpData(String.format(DConstant.DATA_ROOT_DIR,DConstant.AD_PLAN));
        adPlanStrings.forEach(p-> AdLevelDataHandler.handleLevel2(JSON.parseObject(p, AdPlanTable.class), OpType.ADD));
        List<String> adCreativeStrings = loadDumpData(String.format(DConstant.DATA_ROOT_DIR,DConstant.AD_CREATIVE));
        adCreativeStrings.forEach(c->AdLevelDataHandler.handleLevel2(JSON.parseObject(c,AdCreativeTable.class),OpType.ADD));

        //Load level3 index from file
        List<String> adUnitStrings = loadDumpData(String.format(DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT));
        adUnitStrings.forEach(u->AdLevelDataHandler.handleLevel3(JSON.parseObject(u,AdUnitTable.class),OpType.ADD));
        List<String> creativeUnitString = loadDumpData(String.format(DConstant.DATA_ROOT_DIR,DConstant.AD_CREATIVE_UNIT));
        creativeUnitString.forEach(c->AdLevelDataHandler.handleLevel3(JSON.parseObject(c, AdCreativeUnitTable.class),OpType.ADD));

        //Load level4 index from file
        List<String> unitKeywordStrings = loadDumpData(String.format(DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_KEYWORD));
        unitKeywordStrings.forEach(k->AdLevelDataHandler.handlerLevel4(JSON.parseObject(k, AdUnitKeywordTable.class),OpType.ADD));

        List<String> unitItStrings = loadDumpData(String.format(DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_IT));
        unitItStrings.forEach(i->AdLevelDataHandler.handlerLevel4(JSON.parseObject(i,AdUnitItTable.class),OpType.ADD));

        List<String> unitDistrictStrings = loadDumpData(String.format(DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_DISTRICT));
        unitDistrictStrings.forEach(d->AdLevelDataHandler.handlerLevel4(JSON.parseObject(d,AdUnitDistrictTable.class),OpType.ADD));

    }

    private List<String> loadDumpData(String filename){
        try(BufferedReader br = Files.newBufferedReader(Paths.get(filename))){
            return br.lines().collect(Collectors.toList());
        }catch (IOException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
}
