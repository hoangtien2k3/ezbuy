package com.ezbuy.productmodel.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchProductRequest {
    private String name; // ten hang hoa
    private String code; // ma hang hoa
    private String unit; // don vi tinh
    private Integer lockStatus; // trang thai khoa: 1 = da khoa, 0 = khong khoa
    private Integer pageIndex; // so trang
    private Integer pageSize; // kich thuoc trang
    private List<String> sort; // sap xep theo truong
}
