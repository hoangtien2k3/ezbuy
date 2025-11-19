package com.ezbuy.cartservice.domain.dto.response;

import com.ezbuy.cartservice.domain.dto.CartTelecomDTO;
import com.ezbuy.cartservice.domain.dto.PaginationDTO;
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
