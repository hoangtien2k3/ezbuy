package com.ezbuy.productmodel.model;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product")
public class Product implements Persistable<String> {
    @Id
    private String id;

    private String code; // ma hang hoa
    private String name; // ten hang hoa
    private Double priceImport; // don gia nhap
    private Double priceExport; // don gia ban
    private String unit; // don vi tinh
    private String taxRatio; // thue GTGT
    private Double discount; // chiet khau
    private Long revenueRatio; // ti le % theo doanh thu
    private Integer status; // trang thai: 1 = hieu luc, 0 = khong hieu luc
    private Integer lockStatus; // trang thai khoa: 1 = da khoa, 0 = khong khoa
    private String createUnit; // ten don vi tao
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
    private String organizationId; // ma to chuc tao

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
        if (this.name != null) {
            this.name = this.name.trim();
        }
        if (this.unit != null) {
            this.unit = this.unit.trim();
        }
    }
}
