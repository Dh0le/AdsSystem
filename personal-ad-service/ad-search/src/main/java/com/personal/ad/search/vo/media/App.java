package com.personal.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class App {
    // identification code of the app that made this request.
    private String appCode;

    // application name;
    private String appName;

    private String packageName;

    private String activityName;
}
