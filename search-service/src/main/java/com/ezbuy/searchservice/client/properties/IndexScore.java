package com.ezbuy.searchservice.client.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexScore {
    private String name;
    private String score;
}
