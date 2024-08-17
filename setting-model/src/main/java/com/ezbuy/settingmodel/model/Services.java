package com.ezbuy.settingmodel.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "services", type = "services")
public class Services {
    @Id
    private String id;
    private String title;
    private String content;
    private String subTitle;
    private String code;
    private LocalDateTime updateAt;
}
