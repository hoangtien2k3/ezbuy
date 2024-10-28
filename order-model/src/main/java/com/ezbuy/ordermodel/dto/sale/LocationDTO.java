package com.ezbuy.ordermodel.dto.sale;

import lombok.Data;

@Data
public class LocationDTO extends BaseDTO {
    protected String code;
    protected String label;
    protected String name;
    protected String vtMapCode;
}
