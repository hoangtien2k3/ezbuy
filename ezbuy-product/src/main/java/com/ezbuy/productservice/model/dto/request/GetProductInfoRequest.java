package com.ezbuy.productservice.model.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductInfoRequest {
    private String type;
    private List<String> ids;
    private UUID organizationId;
    private Integer offset;
    private Integer limit;
    private String transactionId;
}
