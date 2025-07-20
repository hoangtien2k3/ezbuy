package com.ezbuy.cartmodel.dto;

import com.ezbuy.settingmodel.dto.TelecomDTO;
import java.util.List;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CartTelecomDTO extends TelecomDTO {
    private List<CartItemProductDTO> productCartItemList;
}
