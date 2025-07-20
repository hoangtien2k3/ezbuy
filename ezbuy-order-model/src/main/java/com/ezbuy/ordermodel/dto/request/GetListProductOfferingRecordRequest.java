package com.ezbuy.ordermodel.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetListProductOfferingRecordRequest {
    private String actionCode; // ma hanh dong vd 00 -> dau noi
    private List<String> lstProductOffering; // danh sach ma goi cuoc
}
