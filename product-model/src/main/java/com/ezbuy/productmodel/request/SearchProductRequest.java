package com.ezbuy.productmodel.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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
