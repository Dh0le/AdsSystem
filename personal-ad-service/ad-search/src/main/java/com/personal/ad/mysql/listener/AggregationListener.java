package com.personal.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.personal.ad.mysql.TemplateHolder;
import com.personal.ad.mysql.dto.BinLogRowData;
import com.personal.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener
{
    private String dbName;
    private String tableName;
    private Map<String,Ilistener> listenerMap = new HashedMap<>();


    private TemplateHolder templateHolder;
    @Autowired
    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }

    private String genKey(String dbName, String tableName){
        return dbName + ":" + tableName;
    }

    public void register(String _dbName, String _tableName, Ilistener listener){
        log.info("register->{}-{}",_dbName,_tableName);
        this.listenerMap.put(genKey(_dbName,_tableName),listener);
    }

    @Override
    public void onEvent(Event event) {
        EventType type = event.getHeader().getEventType();
        log.debug("event type:{}",type);
        if(type == EventType.TABLE_MAP){
            TableMapEventData data = event.getData();
            this.tableName = data.getTable();
            this.dbName = data.getDatabase();
            return;
        }
        if(type != EventType.EXT_WRITE_ROWS && type != EventType.EXT_UPDATE_ROWS && type != EventType.EXT_DELETE_ROWS){
            return;
        }

        //check dbname and tableName
        if(StringUtils.isEmpty(this.tableName) || StringUtils.isEmpty(this.dbName)){
            log.error("no meta data of current event");
            return;
        }
        String key = genKey(this.dbName,this.tableName);
        Ilistener ilistener = this.listenerMap.get(key);
        if(ilistener == null){
            log.debug("skip:{}",key);
            return;
        }
        log.info("Trigger event:{}",type.name());
        try{
            BinLogRowData rowData = buildRowData(event.getData());
            if(rowData == null){
                return;
            }
            rowData.setEventType(type);
            ilistener.onEvent(rowData);
        }
        catch (Exception ex){
            ex.printStackTrace();
            log.error(ex.getMessage());
        }finally {
            this.dbName = "";
            this.tableName = "";
        }
    }

    private List<Serializable[]> getAfterValue(EventData eventData){
        if(eventData instanceof WriteRowsEventData){
            return ((WriteRowsEventData) eventData).getRows();
        }
        if(eventData instanceof UpdateRowsEventData){
            return ((UpdateRowsEventData) eventData)
                    .getRows()
                    .stream()
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
        if(eventData instanceof DeleteRowsEventData){
            return ((DeleteRowsEventData) eventData).getRows();
        }else{
            return Collections.emptyList();
        }

    }

    private BinLogRowData buildRowData(EventData eventData){
        TableTemplate table = templateHolder.getTable(tableName);
        if(table == null){
            log.warn("Table {} not found:",tableName);
        }
        List<Map<String,String>> afterMapList = new ArrayList<>();

        for (Serializable[] after : getAfterValue(eventData)) {
            Map<String,String> afterMap = new HashedMap<>();
            int columnLen = after.length;
            for(int ix = 0;ix < columnLen;ix++){
                //Acquire column name according to column position
                String colName = table.getPosMap().get(ix);
                // if colName is null, we don't care about this column
                if(colName == null){
                    log.debug("ignore position:{}",ix);
                }
                String colValue = after[ix].toString();
                afterMap.put(colName,colValue);
            }
            afterMapList.add(afterMap);
            BinLogRowData rowData = new BinLogRowData();
            rowData.setAfter(afterMapList);
            rowData.setTable(table);

            return rowData;
            
        }
        return null;
    }

}
