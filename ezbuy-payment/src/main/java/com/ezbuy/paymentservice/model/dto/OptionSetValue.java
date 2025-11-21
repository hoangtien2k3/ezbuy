package com.ezbuy.paymentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OptionSetValue {
    @Id
    private String id;

    private Long optionSetId;
    private String code;
    private String value;
    private String description;

    @Transient
    private boolean isNew = false;
}
