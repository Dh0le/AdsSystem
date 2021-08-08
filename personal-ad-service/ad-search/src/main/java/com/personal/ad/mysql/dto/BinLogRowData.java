package com.personal.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinLogRowData {
    private TableTemplate table;
    private EventType eventType;
    private List<Map<String,String>> after;
    private List<Map<String,String>> before;
}
