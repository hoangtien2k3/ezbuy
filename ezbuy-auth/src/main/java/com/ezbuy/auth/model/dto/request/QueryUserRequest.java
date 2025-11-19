package com.ezbuy.auth.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class QueryUserRequest {
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fromDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate toDate;

    private String name;
    private String provinceCode;
    private String taxCode;
    private String phoneNumber;
    private String companyName;
    private Integer pageIndex;
    private Integer pageSize;
    private List<String> userIds;
    private List<String> ids;
    private String sort;
}
