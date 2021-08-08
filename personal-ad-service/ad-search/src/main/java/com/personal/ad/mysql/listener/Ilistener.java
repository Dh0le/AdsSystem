package com.personal.ad.mysql.listener;

import com.personal.ad.mysql.dto.BinLogRowData;

public interface Ilistener {
    void register();
    void onEvent(BinLogRowData eventData);
}
