package com.ezbuy.authmodel.dto;

import com.ezbuy.authmodel.model.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileDTO extends UserProfile {
    private String name;
}
