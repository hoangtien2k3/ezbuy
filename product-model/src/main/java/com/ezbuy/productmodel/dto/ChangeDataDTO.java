package com.ezbuy.productmodel.dto;

import com.ezbuy.productmodel.response.PaginationDTO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeDataDTO {
    private String syncType;
    private PaginationDTO pagination; // phan trang
    private String objectType; // doi tuong thuc hien dong bo
    private LocalDateTime changeDate;
    private List<String> ids; // danh sach id khach hang
    private Integer order; // thu tu dong bo
    private String sourceAlias; // nguon lay du lieu
}
