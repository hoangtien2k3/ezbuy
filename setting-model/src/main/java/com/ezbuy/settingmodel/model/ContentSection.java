package com.ezbuy.settingmodel.model;

import com.ezbuy.settingmodel.model.base.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "content_section")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentSection extends EntityBase {
    @Id
    private String id;
    private String parentId;
    private String sectionId;
    private String type;
    private String refId;
    private String refAlias;
    private String refType;
    private String name;
    private Long displayOrder;
    private String path;
//    private Integer status;
//    private String createBy;
//    private LocalDateTime createAt;
//    private String updateBy;
//    private LocalDateTime updateAt;
}
