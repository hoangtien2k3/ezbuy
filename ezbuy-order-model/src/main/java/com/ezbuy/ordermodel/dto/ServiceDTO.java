package com.ezbuy.ordermodel.dto;

import lombok.Data;

// DTO cho dich vu
// bo sung them alias PYCXXX/LuongToanTrinhScontract
@Data
public class ServiceDTO {

    private String telecomServiceId; // originalId cua dich vu PYCXXX/LuongToanTrinhScontract
    private String telecomServiceAlias; // alias cua dich vu PYCXXX/LuongToanTrinhScontract
}
