package com.ezbuy.cartservice.application.dto.response;

import com.ezbuy.cartservice.application.dto.CartTelecomDTO;
import com.ezbuy.cartservice.application.dto.PaginationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageCart {
    private List<CartTelecomDTO> listItem;
    private Integer totalCount;
    private Long totalPrice;
    private PaginationDTO pagination;
}
