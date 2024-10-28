package com.ezbuy.ordermodel.dto.request;

import com.ezbuy.ordermodel.dto.AddressDTO;
import com.ezbuy.ordermodel.dto.ProductOrderItem;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreatePaidOrderRequest {

    @Length(max = 36, message = "order.idNo.over.length")
    private String idNo;

    private String orderType;
    private Integer groupType;

    @Valid
    private List<ProductOrderItem> productOrderItems;

    private String returnUrl;
    private String cancelUrl;
    private String name;
    private String email;
    private String phoneNumber;
    private AddressDTO address;
    private String fromStaff; // nhan vien moi gioi
    private String organizationId; // id to chuc khach hang chon
}
