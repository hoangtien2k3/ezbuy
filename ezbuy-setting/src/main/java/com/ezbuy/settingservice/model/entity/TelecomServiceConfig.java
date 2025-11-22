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
@Table(name = "telecom_service_config")
public class TelecomServiceConfig extends EntityBase {
    @Id
    private String id;

    private String config;
    private String telecomServiceId;
}
