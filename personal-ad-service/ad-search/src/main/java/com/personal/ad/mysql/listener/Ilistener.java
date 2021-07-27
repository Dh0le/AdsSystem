package com.personal.ad.mysql.listener;

import com.personal.ad.mysql.dto.BInLogRowData;

public interface Ilistener {
    void register();
    void onEvent(BInLogRowData eventData);
}
