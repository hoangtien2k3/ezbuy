package com.ezbuy.settingmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeDataDTO {
    private String title;
    private String key;
    private List<TreeDataDTO> children;
}
