package com.personal.ad.sender;

import com.personal.ad.mysql.dto.MySqlRowData;

public interface ISender {
    void sender(MySqlRowData rowData);
}
