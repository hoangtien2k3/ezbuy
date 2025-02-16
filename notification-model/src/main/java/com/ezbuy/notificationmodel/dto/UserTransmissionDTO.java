package com.ezbuy.notificationmodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class UserTransmissionDTO {
    @JsonIgnoreProperties("transmission_id")
    private String transmissionId;

    private String email;

    @JsonProperty("template_mail")
    private String templateMail;

    @JsonProperty("create_at")
    private LocalDateTime createAt;

    private String state;

    @JsonProperty("create_by")
    private String createBy;
}
