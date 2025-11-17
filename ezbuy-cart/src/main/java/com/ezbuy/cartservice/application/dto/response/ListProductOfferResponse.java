package com.ezbuy.cartservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListProductOfferResponse {
    private List<ProductOfferTemplateDTO> data;
}
