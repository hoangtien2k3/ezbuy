package com.ezbuy.productmodel.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceSyncProductDTO {
    private String type;
    private List<TelecomIdentifyDTO> list;
}
