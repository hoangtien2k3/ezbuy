package com.ezbuy.productservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "voucher_transaction")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherTransaction implements Persistable<String> {
    @Id
    private String id;

    private String voucherId;
    private String userId;
    private String transactionCode;
    private LocalDateTime transactionDate;
    private String transactionType;
    private Integer amount;
    private String state;
    private String createBy;
    private String updateBy;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
