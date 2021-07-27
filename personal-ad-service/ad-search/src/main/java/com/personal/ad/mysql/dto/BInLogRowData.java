package com.personal.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BInLogRowData {
    private TableTemplate table;
    private EventType eventType;
    private List<Map<String,String>> after;
    private List<Map<String,String>> before;
}
