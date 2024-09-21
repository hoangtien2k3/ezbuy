package com.ezbuy.notimodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailResultDTO {

    private String transmissionId;
    private Boolean isSuccess;
}
