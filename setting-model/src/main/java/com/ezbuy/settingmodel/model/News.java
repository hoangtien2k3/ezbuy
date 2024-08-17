package com.ezbuy.settingmodel.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("news")
@Document(indexName = "news", type = "news")
public class News {
    @Id
    private String id;
    private String title;
    private String content;
    private String path;
    private String newsType;
    private String sourceType;
    private LocalDateTime updateAt;
}
