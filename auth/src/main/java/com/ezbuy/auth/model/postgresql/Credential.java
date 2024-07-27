package com.ezbuy.auth.model.postgresql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "CREDENTIAL")
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
