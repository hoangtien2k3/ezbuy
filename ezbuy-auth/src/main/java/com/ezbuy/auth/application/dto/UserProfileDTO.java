package com.ezbuy.auth.application.dto;

import com.ezbuy.auth.domain.model.entity.UserProfileEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileDTO extends UserProfileEntity {
    private String name;
}
