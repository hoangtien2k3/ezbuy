package com.ezbuy.authmodel.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
