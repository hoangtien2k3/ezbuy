package com.ezbuy.ordermodel.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class SearchOrderV2Request extends SearchOrderRequest {

    List<String> preOrderCodeList;
}
