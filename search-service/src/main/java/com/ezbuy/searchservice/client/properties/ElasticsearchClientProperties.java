package com.ezbuy.searchservice.client.properties;

import com.ezbuy.reactify.client.properties.WebClientProperties;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component("elasticsearchClientProperties")
@ConfigurationProperties(prefix = "client.elasticsearch", ignoreInvalidFields = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class ElasticsearchClientProperties extends WebClientProperties {
    private String username;
    private String password;
}
