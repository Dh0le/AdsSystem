package com.personal.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.personal.ad.mysql.constant.Constant;
import com.personal.ad.mysql.constant.OpType;
import com.personal.ad.mysql.dto.BinLogRowData;
import com.personal.ad.mysql.dto.MySqlRowData;
import com.personal.ad.mysql.dto.TableTemplate;
import com.personal.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IncrementListener implements Ilistener{

    private final AggregationListener aggregationListener;
    @Resource(name = "")
    private ISender sender;

    @Autowired
    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }

    @Override
    @PostConstruct
    public void register() {
        log.info("IncrementListener register db and table info");
        Constant.table2DB.forEach((k,v)->aggregationListener.register(v,k,this));
    }

    @Override
    public void onEvent(BinLogRowData eventData) {
        TableTemplate table = eventData.getTable();
        EventType eventType = eventData.getEventType();

        //pack the data into data ready to be sent.
        MySqlRowData rowData = new MySqlRowData();
        rowData.setTableName(table.getTableName());
        rowData.setLevel(eventData.getTable().getLevel());
        OpType type = OpType.to(eventType);
        rowData.setOpType(type);

        //acquire corresponding column list

        List<String> fieldList = table.getOpTypeFieldSetMap().get(type);
        if(fieldList == null){
            log.warn("{} Not support for {}",type,table.getTableName());
        }
        for(Map<String,String> afterMap:eventData.getAfter()){
            Map<String,String> _afterMap = new HashMap<>();
            for(Map.Entry<String,String>entry:afterMap.entrySet()){
                String colName = entry.getKey();
                String colVal = entry.getValue();
                _afterMap.put(colName,colVal);
            }
            rowData.getFieldValueMap().add(_afterMap);
        }
        sender.sender(rowData);
    }
}
