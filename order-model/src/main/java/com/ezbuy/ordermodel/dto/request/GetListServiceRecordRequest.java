package com.ezbuy.ordermodel.dto.request;

import com.ezbuy.ordermodel.dto.ws.lstServiceDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetListServiceRecordRequest {
    private String actionCode; // ma hanh dong vd: 00-dau noi
    private List<lstServiceDTO> lstService; // danh sach dich vu
}
