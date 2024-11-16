package com.ezbuy.searchservice.client.properties;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "application.index")
public class IndexProperties {
    private List<IndexScore> indexScores;
}
