package com.ezbuy.usermodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_settings")
public class UserSetting {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @Column(length = 50)
    @JsonProperty("setting_key")
    private String settingKey;

    @JsonProperty("setting_value")
    private String settingValue;
}
