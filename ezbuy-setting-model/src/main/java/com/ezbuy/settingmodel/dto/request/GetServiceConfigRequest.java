package com.ezbuy.settingmodel.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetServiceConfigRequest {
    private List<String> lstServiceAlias;
    private String syncType; // loai du lieu dong bo (organization, policy, password,...)
}
