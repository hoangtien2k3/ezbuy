package com.ezbuy.ordermodel.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceItemDTO {

    private Long telecomServiceId;
    private String name;
    private String telecomServiceAlias; // bo sung theo PYCXXX/LuongToanTrinhScontract
    private List<OrderFileDTO> uploadFile; // danh sach ho so dich vu
}
