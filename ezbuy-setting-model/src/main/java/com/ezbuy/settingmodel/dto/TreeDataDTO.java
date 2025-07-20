package com.ezbuy.settingmodel.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeDataDTO {
    private String title;
    private String key;
    private List<TreeDataDTO> children;
}
