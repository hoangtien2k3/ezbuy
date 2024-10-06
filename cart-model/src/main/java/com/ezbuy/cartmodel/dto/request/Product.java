package com.ezbuy.cartmodel.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @NotEmpty(message = "product.not.null")
    private List<String> listProductId;

    @NotEmpty(message = "telecom.id.not.null")
    private String telecomServiceId;

    @NotEmpty(message = "telecom.alias.not.null")
    private String telecomServiceAlias; //Bo sung Alias PYCXXX/LuongToanTrinh
}
