package com.ezbuy.productmodel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "voucher_use")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherUse implements Persistable<String> {
    @Id
    private String id;
    private String voucherId; //id bang voucher
    private String userId; //id user dang nhap
    private String username;
    private String systemType; //he thong ap dung voucher
    private LocalDateTime createDate; //ngay bat dau gan voucher cho user
    private LocalDateTime expiredDate; //ngay het han sau khi da tinh toan
    private String state; //preActive, active, inactive, used
    private String sourceOrderId; //id bang sme_order.order
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
