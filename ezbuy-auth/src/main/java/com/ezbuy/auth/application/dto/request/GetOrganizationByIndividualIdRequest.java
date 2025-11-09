package com.ezbuy.auth.application.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetOrganizationByIndividualIdRequest {
    @NotEmpty(message = "individual.id.not.empty")
    private String individualId;

    public void setIndividualId(@NonNull String individualId) {
        this.individualId = individualId.trim();
    }
}
