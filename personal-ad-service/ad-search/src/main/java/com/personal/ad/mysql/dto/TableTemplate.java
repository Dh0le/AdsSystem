package com.personal.ad.mysql.dto;

import com.personal.ad.mysql.constant.OpType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TableTemplate {
    private String tableName;
    private String level;

    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();

    /**
     *
     * Column index -> column name;
     */
    private Map<Integer, String> posMap = new HashMap<>();

}
