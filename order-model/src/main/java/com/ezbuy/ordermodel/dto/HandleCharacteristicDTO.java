package com.ezbuy.ordermodel.dto;

import com.ezbuy.ordermodel.dto.sale.GroupsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandleCharacteristicDTO {
    // thuoc tinh mo rong cua dich vu
    private List<CharacteristicDTO> productCharacteristic;
    // thuoc tinh cua san pham tren bccs product
    private ProductOfferingRef productOfferingRef;
    // thuoc tinh cua product
    private GroupsDTO groupsDTO;
}
