package com.ezbuy.ordermodel.dto;

import lombok.Data;

@Data
public class CoordinatesDTO {
    private String latitude;
    private String longitude;
    private String height;
    private String width;
    private Long pageNumber;
    private String signType;
    private String documentNumber;
    private String username;
}
