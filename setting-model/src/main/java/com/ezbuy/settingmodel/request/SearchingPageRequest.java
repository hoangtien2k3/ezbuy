package com.ezbuy.settingmodel.request;

import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchingPageRequest extends PageRequest {
    private String title;
    private String code;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;
    @Past(message = "params.to-date.not.future")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;
    private Boolean status;
}
