package com.ezbuy.settingmodel.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentSectionRequest {
    private String id;
    private String parentId;
    private String sectionId;
    private String type;
    private String refId;
    private String refAlias; // alias cua dich vu PYCXXX/LuongToanTrinhScontract
    private String refType;
    private String name;
    private Long displayOrder;
    private String path;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;

    private Integer status;
}
