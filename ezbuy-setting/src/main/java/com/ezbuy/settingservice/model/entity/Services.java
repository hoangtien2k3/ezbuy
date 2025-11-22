package com.ezbuy.settingservice.model.entity;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Services {
    @Id
    private String id;

    private String title;
    private String content;
    private String subTitle;
    private String code;
    private LocalDateTime updateAt;
}
