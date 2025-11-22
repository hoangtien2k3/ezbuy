package com.ezbuy.settingservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "group_news")
public class GroupNews extends EntityBase {
    @Id
    private String id;

    private String name; // ten nhom tin tuc
    private String code; // ma nhom tin tuc
    private Integer displayOrder; // thu tu sap xep
}
