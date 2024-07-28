package com.ezbuy.auth.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ActionLogRequest {

    private String username;
    private String ip;
    private String type;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fromDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate toDate;
    private String sort;
    private Integer pageIndex;
    private Integer pageSize;

}
