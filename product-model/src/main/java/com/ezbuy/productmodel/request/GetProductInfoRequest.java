package com.ezbuy.productmodel.request;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductInfoRequest {
    private String type; // loai khach hang
    private List<String> ids; // danh sach id
    private UUID organizationId; // id to chuc
    private Integer offset; // vi tri phan trang
    private Integer limit; // tong so ban ghi lay ra
    private String transactionId; // transId cua ban tin request
}
