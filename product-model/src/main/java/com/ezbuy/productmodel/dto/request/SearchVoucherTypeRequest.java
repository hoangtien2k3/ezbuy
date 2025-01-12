package com.ezbuy.productmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchVoucherTypeRequest {
    private String sort;//sort theo truong nao
    private Integer pageIndex;//phan trang pageIndex
    private Integer pageSize;//phan trang pageSize
    private String code;//ma voucher type
    private String name;//ten voucher type
    private Integer priorityLevel;//do uu tien
    private String description;//mo ta chi tiet
    private String actionType;//loai khuyen mai
    private String actionValue;//gia tri
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate createFromDate;//tu ngay tao
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate createToDate;//den ngay tao
    private String state;//trang thai vat ly
    private Integer status;//trang thai
}
