package com.ezbuy.productmodel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "voucher_batch")
public class VoucherBatch implements Persistable<String> {
    @Id
    private String id; // uuid

    private String code; // ma lo voucher
    private String description; // mo ta
    private String voucherTypeId; // ID voucher type
    private Integer quantity;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime expiredDate; // ngay het han

    private Integer expiredPeriod; // thoi gian het han
    private String state; // trang thai lo voucher
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }

    public void trim() {
        if (this.code != null) {
            this.code = this.code.trim();
        }
    }
}
