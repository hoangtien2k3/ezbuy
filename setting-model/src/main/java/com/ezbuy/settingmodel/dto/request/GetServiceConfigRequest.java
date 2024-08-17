package com.ezbuy.settingmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetServiceConfigRequest {
    private List<String> lstServiceAlias;
    private String syncType; // loai du lieu dong bo (organization, policy, password,...)
}
