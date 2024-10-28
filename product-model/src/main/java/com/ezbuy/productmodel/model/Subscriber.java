package com.ezbuy.productmodel.model;

import java.time.LocalDate;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "subscriber")
public class Subscriber implements Persistable<String> {

    @Id
    private String id;

    private String productId;

    private String productCode;

    private String productName;

    private Long telecomServiceId;

    private String idNo;

    private String isdn;

    private Integer status;

    private LocalDate activationDate;

    private LocalDate expiredDate;

    private String address;

    private Integer groupType;

    private Long accountId;

    private String telecomServiceAlias;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
