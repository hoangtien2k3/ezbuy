package com.ezbuy.settingmodel.model;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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
