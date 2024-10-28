package com.ezbuy.cartmodel.dto;

import com.ezbuy.settingmodel.dto.TelecomDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CartTelecomDTO extends TelecomDTO {
    private List<CartItemProductDTO> productCartItemList;
}
