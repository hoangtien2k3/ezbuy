package com.ezbuy.customer.model.dto;

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
