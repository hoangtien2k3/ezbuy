package com.ezbuy.productmodel.model;

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
@Table(name = "voucher_use")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherUse implements Persistable<String> {
    @Id
    private String id;

    private String voucherId;
    private String userId;
    private String username;
    private String systemType;
    private LocalDateTime createDate;
    private LocalDateTime expiredDate;
    private String state;
    private String sourceOrderId;
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
