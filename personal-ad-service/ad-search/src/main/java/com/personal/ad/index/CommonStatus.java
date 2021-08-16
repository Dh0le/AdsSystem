package com.personal.ad.index;

import lombok.Getter;

@Getter
public enum CommonStatus {
    VALID(1,"valid"),
    INVALID(0,"invalid");

    private int status;
    private String description;

    CommonStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }
}
