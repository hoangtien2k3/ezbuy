package com.ezbuy.ordermodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "order_ext")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderExt implements Persistable<String> {
    @Id
    private String id;
    private String orderId; // id bang order
    private String code;
    private String value;
    private Integer status;
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
}
