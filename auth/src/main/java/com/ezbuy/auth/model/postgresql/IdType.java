package com.ezbuy.auth.model.postgresql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "id_type")
@Builder
public class IdType {
    @Id
    private String id;
    private String name;
    private String code;
    private Integer status;
}
