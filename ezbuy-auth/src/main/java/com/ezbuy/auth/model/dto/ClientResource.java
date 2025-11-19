package com.ezbuy.auth.model.dto;

import java.util.List;
import lombok.Data;

@Data
public class ClientResource {
    private String id;
    private String name;
    private String type;
    private List<ResourcePermission> permissions;
}
