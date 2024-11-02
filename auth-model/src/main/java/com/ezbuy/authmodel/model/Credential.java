package com.ezbuy.authmodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "credential")
public class Credential {
    private String id;
    private String type;
    private String userId;
    private String secretData;
    private String credentialData;
}
