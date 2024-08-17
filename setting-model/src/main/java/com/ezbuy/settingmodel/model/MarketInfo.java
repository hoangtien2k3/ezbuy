package com.ezbuy.settingmodel.model;

import com.ezbuy.settingmodel.model.base.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "market_info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketInfo extends EntityBase {
    @Id
    private String id;
    private String serviceId;
    private String serviceAlias; // alias of service PYCXXX/LuongToanTrinhScontract
    private String title;
    private String navigatorUrl;
    private Integer marketOrder;
    private String marketImageUrl;
}
