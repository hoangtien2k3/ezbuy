package com.ezbuy.productservice.model.entity;

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

    private String code;
    private String name;
    private Double priceImport;
    private Double priceExport;
    private String unit;
    private String taxRatio;
    private Double discount;
    private Long revenueRatio;
    private Integer status;
    private Integer lockStatus;
    private String createUnit;
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
    private String organizationId;

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
