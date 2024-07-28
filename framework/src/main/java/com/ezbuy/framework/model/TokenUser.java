package com.ezbuy.framework.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenUser {
    private String username;
    private String id; // userId in table user_entity of schema sme_keycloak
    private String name;
    private String email;
    @JsonProperty("individual_id")
    private String individualId;
    private String organizationId;
}
