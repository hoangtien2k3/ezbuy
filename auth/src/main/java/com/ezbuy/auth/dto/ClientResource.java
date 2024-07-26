package com.ezbuy.auth.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClientResource {

    private String id;
    private String name;
    private String type;
    private List<ResourcePermission> permissions;

}
