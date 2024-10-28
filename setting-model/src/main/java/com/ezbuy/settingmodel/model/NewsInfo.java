package com.ezbuy.settingmodel.model;

import com.ezbuy.settingmodel.model.base.EntityBase;
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
@Table(name = "news_info")
public class NewsInfo extends EntityBase {
    @Id
    private String id;

    private String title; // tieu de
    private String code; // ma thong tin
    private Integer displayOrder; // thu tu sap xep
    private String groupNewsId; // id nhom tin tuc
    private String summary; // tom tat noi dung
    private String navigatorUrl; // anh dai dien
    private String state; // trang thai nghiep vu
}
