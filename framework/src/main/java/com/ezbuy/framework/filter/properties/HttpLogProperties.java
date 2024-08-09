package com.ezbuy.framework.filter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ezbuy.framework.model.logging.HttpLogRequest;
import com.ezbuy.framework.model.logging.HttpLogResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hoangtien2k3
 * <p> http request and http response
 *
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
