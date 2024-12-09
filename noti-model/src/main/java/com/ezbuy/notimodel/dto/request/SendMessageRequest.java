package com.ezbuy.notimodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMessageRequest {
    private String aliasName;
    private List<String> msisdn;
    private String content;
    private Integer convertVN;
}
