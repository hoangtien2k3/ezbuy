package com.ezbuy.settingmodel.response;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TelecomResponse {
    private String id;
    private String name; // ten telecom service
    private String serviceAlias; // ten ung dung
    private String description; // mo ta ung dung
    private String image;
    private String originId; // id that cua telecom service
    private Integer status; // trang thai
    private Boolean isFilter;
    private String marketImageUrl;
    private String groupId;
    private String createBy;
    private String updateBy;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
