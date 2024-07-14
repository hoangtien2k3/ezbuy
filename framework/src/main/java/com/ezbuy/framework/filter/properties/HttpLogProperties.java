package com.ezbuy.framework.filter.properties;

import com.ezbuy.framework.model.logging.HttpLogRequest;
import com.ezbuy.framework.model.logging.HttpLogResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author huannt16
 * <p>
 * cau hinh log cho http request và http response cua ung dung
 */
@Component
@ConfigurationProperties(prefix = "application.http-logging", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogProperties {
    private HttpLogRequest request = new HttpLogRequest();
    private HttpLogResponse response = new HttpLogResponse();
}
