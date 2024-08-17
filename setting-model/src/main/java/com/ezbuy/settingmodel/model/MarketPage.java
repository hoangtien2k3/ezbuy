package com.ezbuy.settingmodel.model;

import com.ezbuy.settingmodel.model.base.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "market_page")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketPage extends EntityBase {
    //Id danh muc market page
    @Id
    private String id;
    // id dich vu lay tu bang telecom service
    private String serviceId;
    private String serviceAlias;
    private String nameService;
    //ma market page
    private String code;
    //ten market page
    private String name;
    //mo ta market page
    private String description;
}
