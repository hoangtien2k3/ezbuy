package com.ezbuy.cartmodel.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DeleteUserProductCartDTO {
    private String userId;
    private List<String> listProductId;
}
