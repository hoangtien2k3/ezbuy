package com.ezbuy.productmodel.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ServiceSyncProductDTO {
    private String type;
    private List<TelecomIdentifyDTO> list;
}
