package com.personal.ad.service;

import com.alibaba.fastjson.JSON;
import com.personal.ad.Application;
import com.personal.ad.constant.CommonStatus;
import com.personal.ad.dao.*;
import com.personal.ad.dump.DConstant;
import com.personal.ad.dump.table.*;
import com.personal.ad.entity.AdPlan;
import com.personal.ad.entity.AdUnit;
import com.personal.ad.entity.Creative;
import com.personal.ad.entity.CreativeUnit;
import com.personal.ad.entity.unit_condition.AdUnitDistrict;
import com.personal.ad.entity.unit_condition.AdUnitIt;
import com.personal.ad.entity.unit_condition.AdUnitKeyword;
import com.personal.ad.vo.CreativeUnitRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DumpDataService {
    @Autowired
    private AdPlanRepository planRepository;
    @Autowired
    private AdUnitRepository unitRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitItRepository itRepository;
    @Autowired
    private AdUnitDistrictRepository unitDistrictRepository;
    @Autowired
    private AdUnitKeywordRepository unitKeywordRepository;

    @Test
    public void dumpAdTableData(){
        dumpAdPlanTable(
                String.format("%s%s", DConstant.DATA_ROOT_DIR,DConstant.AD_PLAN)
        );
        dumpAdCreativeTable(
                String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_CREATIVE)
        );
        dumpAdUnitTable(
                String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT)
        );
        dumpAdCreativeUnitTable(
                String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_CREATIVE_UNIT)
        );
        dumpAdUnitKeyword(
                String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_KEYWORD)
        );

        dumpAdUnitItTable(
                String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_IT)
        );
        dumpAdUnitDistrictTable(
                String.format("%s%s",DConstant.DATA_ROOT_DIR,DConstant.AD_UNIT_DISTRICT)
        );
    }

    private void dumpAdPlanTable(String fileName){
        List<AdPlan> adPlans = planRepository.findAllByPlanStatus(
                CommonStatus.VALID.getStatus()
        );
        if(CollectionUtils.isEmpty(adPlans)){
            return;
        }
        List<AdPlanTable> planTables = new ArrayList<>();
        adPlans.forEach(p->planTables.add(
                new AdPlanTable(p.getId(),
                        p.getUserId(),
                        p.getPlanStatus(),
                        p.getStartDate(),
                        p.getEndDate())
        ));
        Path path = Paths.get(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdPlanTable planTable:planTables){
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
            writer.close();
        }catch (IOException ex){
            log.error("dumpAdPlanTable error");
        }
    }

    private void dumpAdUnitTable(String fileName){
        List<AdUnit> adUnits = unitRepository.findAllByUnitStatus(
                CommonStatus.VALID.getStatus()
        );
        if(CollectionUtils.isEmpty(adUnits)){
            return;
        }
        List<AdUnitTable> unitTables = new ArrayList<>();
        adUnits.forEach(u->unitTables.add(new AdUnitTable(
                u.getId(),
                u.getUnitStatus(),
                u.getPositionType(),
                u.getPlanId()
        )));
        Path path = Paths.get(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdUnitTable unitTable:unitTables){
                writer.write(JSON.toJSONString(unitTable));
                writer.newLine();
            }
            writer.close();
        }catch (IOException ex){
            log.error("dumpAdUnitTable error");
        }
    }

    private void dumpAdCreativeTable(String fileName){
        List<Creative> creatives = creativeRepository.findAll();
        if(CollectionUtils.isEmpty(creatives))return;
        List<AdCreativeTable> creativeTables = new ArrayList<>();
        creatives.forEach(c->creativeTables.add(new AdCreativeTable(
                c.getId(),
                c.getName(),
                c.getType(),
                c.getMaterialType(),
                c.getHeight(),
                c.getWidth(),
                c.getAuditStatus(),
                c.getUrl()
        )));
        Path path = Paths.get(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdCreativeTable creativeTable:creativeTables){
                writer.write(JSON.toJSONString(creativeTable));
                writer.newLine();
            }
            writer.close();
        }catch (IOException ex){
            log.error("dumpCreativeTable error");
        }
    }

    private void dumpAdUnitKeyword(String fileName){
        List<AdUnitKeyword> keywords = unitKeywordRepository.findAll();
        if(CollectionUtils.isEmpty(keywords))return;
        List<AdUnitKeywordTable> keywordTables = new ArrayList<>();
        keywords.forEach(k->keywordTables.add(new AdUnitKeywordTable(
                k.getUnitId(),
                k.getKeyword()
        )));
        Path path = Paths.get(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdUnitKeywordTable keywordTable:keywordTables){
                writer.write(JSON.toJSONString(keywordTable));
                writer.newLine();
            }
            writer.close();
        }catch (IOException ex){
            log.error("dumpKeywordTable error");
        }

    }

    private void dumpAdCreativeUnitTable(String fileName){
        List<CreativeUnit> creativeUnits = creativeUnitRepository.findAll();
        if(CollectionUtils.isEmpty(creativeUnits))return;
        List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
        creativeUnits.forEach(c->creativeUnitTables.add(new AdCreativeUnitTable(
                c.getId(),
                c.getUnitId()
        )));
        Path path = Paths.get(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdCreativeUnitTable creativeUnitTable:creativeUnitTables){
                writer.write(JSON.toJSONString(creativeUnitTable));
                writer.newLine();
            }
            writer.close();
        }catch (IOException ex){
            log.error("dumpAdCreativeUnitTable error");
        }
    }

    private void dumpAdUnitDistrictTable(String fileName){
        List<AdUnitDistrict> districts = unitDistrictRepository.findAll();
        if(CollectionUtils.isEmpty(districts))return;
        List<AdUnitDistrictTable> districtTables = new ArrayList<>();
        districts.forEach(d->districtTables.add(new AdUnitDistrictTable(
                d.getUnitId(),
                d.getProvince(),
                d.getCity()
        )));
        Path path = Paths.get(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdUnitDistrictTable districtTable:districtTables){
                writer.write(JSON.toJSONString(districtTable));
                writer.newLine();
            }
            writer.close();
        }catch (IOException ex){
            log.error("dumpAdUnitDistrictTable error");
        }
    }

    private void dumpAdUnitItTable(String fileName){
        List<AdUnitIt> unitIts = itRepository.findAll();
        if(CollectionUtils.isEmpty(unitIts))return;
        List<AdUnitItTable> itTables = new ArrayList<>();
        unitIts.forEach(i->itTables.add(new AdUnitItTable(
                i.getUnitId(),
                i.getIdTag()
        )));
        Path path = Paths.get(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for(AdUnitItTable itTable:itTables){
                writer.write(JSON.toJSONString(itTable));
                writer.newLine();
            }
            writer.close();
        }catch (IOException ex){
            log.error("dumpAdUnitItTable error");
        }
    }
}
