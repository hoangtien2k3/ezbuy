package com.ezbuy.notimodel.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTransmissionByEmailAndFromToRequest {
    private String username;
    private String email;

    @JsonProperty("template_mail")
    private String templateMail;

    private String from;
    private String to;
    private Integer pageIndex;
    private Integer pageSize;
    private Integer limit;
    private String sort;
}
