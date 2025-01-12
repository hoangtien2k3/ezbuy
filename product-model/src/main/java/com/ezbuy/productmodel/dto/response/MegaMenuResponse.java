package com.ezbuy.productmodel.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MegaMenuResponse {
    private String groupImage;
    private String groupName;
    private String productName;
    private String productDescription;
    private String productLink;
}
