package com.ezbuy.searchservice.client.properties;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "application.index")
public class IndexProperties {
    private List<IndexScore> indexScores;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IndexScore {
        private String name;
        private String score;
    }
}
