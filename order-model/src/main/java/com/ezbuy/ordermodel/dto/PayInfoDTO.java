package com.ezbuy.ordermodel.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PayInfoDTO {

    private Boolean immediatePay;

    private List<Object> cardRecords;

    public PayInfoDTO() {
        immediatePay = false;
        cardRecords = new ArrayList<>();
    }
}
