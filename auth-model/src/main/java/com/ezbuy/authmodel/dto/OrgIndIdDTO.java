package com.ezbuy.authmodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgIndIdDTO {
    private String orgId; // id of doanh nghiep
    private String individualId; // id cua ca nhan trong bang individual
    private String userId; // user_id trong bang individual
}
