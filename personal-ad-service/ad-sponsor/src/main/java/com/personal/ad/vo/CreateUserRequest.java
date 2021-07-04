package com.personal.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String userName;

    public boolean validate(){
        return !StringUtils.isEmpty(userName);
    }

}
