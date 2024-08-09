package com.ezbuy.sendnotification.model.noti;

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
