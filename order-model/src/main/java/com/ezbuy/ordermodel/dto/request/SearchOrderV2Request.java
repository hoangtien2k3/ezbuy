package com.ezbuy.ordermodel.dto.request;


import lombok.Data;

import java.util.List;

@Data
public class SearchOrderV2Request extends SearchOrderRequest {

    List<String> preOrderCodeList;
}
