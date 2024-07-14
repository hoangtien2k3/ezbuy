package com.ezbuy.customermodel.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAddressRequest(
        String street,
        String province,
        String district,
        String precinct,
        String city,
        String addressDetail,
        String postcode,
        String countryCode,
        String company
) {
}
