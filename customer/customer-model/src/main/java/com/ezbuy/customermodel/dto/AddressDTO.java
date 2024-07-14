package com.ezbuy.customermodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddressDTO(
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
