package com.ezbuy.searchservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component("elasticSearchClientProperties")
@ConfigurationProperties(prefix = "client.elasticsearch", ignoreInvalidFields = true)
@AllArgsConstructor
public class ElasticsearchClientProperties extends WebClientProperties {}
