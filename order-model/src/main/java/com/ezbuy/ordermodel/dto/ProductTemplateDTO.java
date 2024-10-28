package com.ezbuy.ordermodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTemplateDTO {

    private String templateCode;

    private Integer quantity;

    private String productOfferingId;

    private Long telecomServiceId;

    private String telecomServiceAlias; // bo sung theo PYCXXX/LuongToanTrinhScontract

    private List<ProductTemplateCharacteristicDTO> productCharacteristic;

    private List<OrderFileDTO> fileUpload; // danh sach ho so thue bao

    @JsonIgnore
    private String templateId;

    @JsonIgnore
    private String name;

    @JsonIgnore
    private String description;

    @JsonIgnore
    private Double price;

    @JsonIgnore
    private Double originPrice;

    @JsonIgnore
    private String duration;
}
