package com.ezbuy.customer.model.dto.request;

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
