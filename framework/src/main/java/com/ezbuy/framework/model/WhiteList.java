package com.ezbuy.framework.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhiteList {
    private String uri;
    private List<String> methods;
}
