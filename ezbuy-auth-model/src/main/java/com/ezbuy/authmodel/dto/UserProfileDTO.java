package com.ezbuy.authmodel.dto;

import com.ezbuy.authmodel.model.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileDTO extends UserProfile {
    private String name;
}
