package com.ezbuy.ordermodel.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "order")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Order {

    private String id;

    private String orderCode;

    private String customerId;

    private Double totalFee;

    private String currency;

    private String areaCode;

    private String province;

    private String district;

    private String precinct;

    private Integer status;

    private Integer state;

    private String description;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;

    private String type;

    private String idNo;

    private String name;

    private String email;

    private String phone;

    private String detailAddress;

    private String individualId; // ma ca nhan tao don hang

    private String logs; // thong tin loi khi goi order tao don hang
}
