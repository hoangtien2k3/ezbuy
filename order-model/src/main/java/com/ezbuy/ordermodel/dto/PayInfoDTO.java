package com.ezbuy.ordermodel.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PayInfoDTO {

    private Boolean immediatePay;

    private List<Object> cardRecords;

    public PayInfoDTO() {
        immediatePay = false;
        cardRecords = new ArrayList<>();
    }

}
