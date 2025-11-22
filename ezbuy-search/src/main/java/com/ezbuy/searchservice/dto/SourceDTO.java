package com.ezbuy.searchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SourceDTO {
    private String id;
    private String title;
    private String content;
    private String code;
    private String path;
    private Long updateAt;
}
