package com.ezbuy.ordermodel.dto.ws;

import com.ezbuy.ordermodel.dto.OrderFileDTO;
import com.ezbuy.ordermodel.dto.ProductOfferingRef;
import com.ezbuy.ordermodel.dto.ProductPaidDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrderItemWsDTO {

    private Long id;

    private Long quantity;

    private String action;

    private ProductPaidDTO product;

    private ProductOfferingRef productOffering;

    private Long subscriberId;

    private String businessCode;

    private List<OrderFileDTO> fileUpload;
}
