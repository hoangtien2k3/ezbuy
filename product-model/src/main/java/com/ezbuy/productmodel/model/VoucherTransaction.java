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
@Table(name = "voucher_transaction")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherTransaction implements Persistable<String> {
    @Id
    private String id;
    private String voucherId; //id bang voucher
    private String userId; //id user dang nhap
    private String transactionCode; //ma don hang su dung voucher
    private LocalDateTime transactionDate; // ngay dat don
    private String transactionType; // loại giao dich (CONNECT- dau noi, AFTER - sau ban)
    private Integer amount; // tong gia giam
    private String state; // trang thai giao dich (preActive - chua thanh toan, used - da thanh toan, inactive - chua thanh toan)
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
