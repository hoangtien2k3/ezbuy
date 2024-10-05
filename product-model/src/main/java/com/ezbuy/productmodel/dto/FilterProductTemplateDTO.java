package com.ezbuy.productmodel.dto;

import com.ezbuy.productmodel.request.ApiUtils;
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
    private ApiUtils utils;
    private List<String> listId;
    private List<String> priceTypes; //danh sach loai gia
}
