package com.ezbuy.notificationmodel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SmsBrandNameLoginResponse {
    private String code; //ma loi tra ve
    private String message; //message tra ve
    private AccessToken data;

    @Getter
    public static class AccessToken {
        @JsonProperty("access_token")
        private String token;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("token_type")
        private String tokenType;
    }
}
