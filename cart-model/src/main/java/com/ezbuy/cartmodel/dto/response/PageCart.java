package com.ezbuy.cartmodel.dto.response;

import com.ezbuy.cartmodel.dto.CartTelecomDTO;
import com.ezbuy.ordermodel.dto.PaginationDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageCart {
    private List<CartTelecomDTO> listItem;
    private Integer totalCount;
    private Long totalPrice;
    private PaginationDTO pagination;
}
