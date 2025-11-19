package com.ezbuy.cartservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterProductTemplateDTO {
    private String telecomServiceAlias;
    private List<String> listId;
    private List<String> priceTypes;
}
