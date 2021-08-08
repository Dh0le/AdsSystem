package com.personal.ad.mysql.constant;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    private static String DB_NAME = "imooc_ad_data";
    private static class AD_PLAN_TABLE_INFO{
        public static final String TABLE_NAME = "ad_plan";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_PLAN_STATUS = "plan_status";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
    }

    private static class AD_CREATIVE_TABLE_INFO{
        public static final String TABLE_NAME = "ad_creative";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WIDTH = "width";
        public static final String COLUMN_AUDIT_STATUS = "audit_status";
        public static final String COLUMN_URL = "url";
    }

    private static class AD_UNIT_TABLE_INFO{
        public static final String TABLE_NAME = "ad_unit";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PLAN_ID  = "plan_id";
        public static final String COLUMN_UNIT_STATUS = "unit_status";
        public static final String COLUMN_POSITION_TYPE = "position_type";
    }

    private static class AD_CREATIVE_UNIT_TABLE_INFO{
        public static final String TABLE_NAME = "creative_unit";
        public static final String COLUMN_CREATIVE_ID = "creative_id";
        public static final String COLUMN_UNIT_ID = "unit_id";
    }

    private static class AD_UNIT_DISTRICT_INFO{
        public static final String TABLE_NAME = "ad_unit_district";
        public static final String COLUMN_UNIT_ID  = "unit_id";
        public static final String COLUMN_PROVINCE  = "province";
        public static final String COLUMN_CITY = "city";
    }

    private static class AD_UNIT_IT_INFO{
        public static final String TABLE_NAME = "ad_unit_it";
        public static final String COLUMN_UNIT_ID  = "unit_id";
        public static final String COLUMN_IT_TAG  = "it_tag";
    }
    private static class AD_UNIT_KEYWORD_INFO {
        public static final String TABLE_NAME = "ad_unit_keyword";
        public static final String COLUMN_UNIT_ID = "unit_id";
        public static final String COLUMN_KEYWORD = "keyword";
    }
    public static Map<String,String> table2DB;
    static{
        table2DB = new HashMap<>();

        table2DB.put(AD_PLAN_TABLE_INFO.TABLE_NAME,DB_NAME);
        table2DB.put(AD_CREATIVE_TABLE_INFO.TABLE_NAME,DB_NAME);
        table2DB.put(AD_UNIT_TABLE_INFO.TABLE_NAME,DB_NAME);
        table2DB.put(AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME,DB_NAME);
        table2DB.put(AD_UNIT_DISTRICT_INFO.TABLE_NAME,DB_NAME);
        table2DB.put(AD_UNIT_IT_INFO.TABLE_NAME,DB_NAME);
        table2DB.put(AD_UNIT_KEYWORD_INFO.TABLE_NAME,DB_NAME);
    }

}
