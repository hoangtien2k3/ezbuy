package com.ezbuy.searchservice.client.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "application.index")
public class IndexProperties {
    private List<IndexScore> indexScores;
}
