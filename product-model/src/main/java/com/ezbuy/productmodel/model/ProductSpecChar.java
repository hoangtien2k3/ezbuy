package com.ezbuy.productmodel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unuse")
@Table(name = "product_spec_char")
public class ProductSpecChar implements Persistable<String> {

    private String id;
    private String code;
    private String name;
    private String telecomServiceId;
    private String telecomServiceAlias;
    private Integer status;
    private Integer state;
    private Integer displayOrder;
    private String viewType;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
