package com.ezbuy.auth.model.postgresql;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "credential")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Credential {
    private String id;
    private String type;
    private String userId;
    private String secretData;
    private String credentialData;
}
