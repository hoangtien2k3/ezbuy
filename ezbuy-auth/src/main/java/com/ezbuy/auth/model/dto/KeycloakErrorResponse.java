package com.ezbuy.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class KeycloakErrorResponse extends AccessToken {
    @JsonProperty("error")
    protected String error;

    @JsonProperty("error_description")
    protected String errorDescription;
}
