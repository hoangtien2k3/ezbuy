package com.ezbuy.productmodel.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class QueryReport {
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fromDateParse; // tu ngay

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate toDateParse; // den ngay

    private String access; // so luong truy cap
    private String totalProducts; // tong so san pham(sku)
    private String newProducts; // so san pham dang ban moi
    private String totalOrders; // tong so don hang
    private String successfulOrders; // tong so don hang thanh fcong
    private String failedOrders; // tong so don hang khong thanh cong
    private String transactionValue; // tong gia tri giao dich
}
