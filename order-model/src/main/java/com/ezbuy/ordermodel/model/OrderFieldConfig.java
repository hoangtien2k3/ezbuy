package com.ezbuy.ordermodel.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_field_config")
public class OrderFieldConfig {

    private Long id;

    private Integer name;
    private Integer email;
    private Integer phone;
    private Integer identity;
    private Integer taxCode;

    private Integer areaCode;
    private Integer detailAddress;

    private Integer status;

    private LocalDateTime createAt;

    private String createdBy;

    private LocalDateTime updateAt;

    private String updateBy;
    private Integer fromStaff; // nhan vien moi gioi
    private String serviceId; // originalId cua dich vu bo sung PYCXXX/LuongToanTrinhScontract
    private String serviceAlias; // alias cua dich vu PYCXXX/LuongToanTrinhScontract
    private Integer amStaff; // ma am ho tro
}
