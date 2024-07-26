package com.ezbuy.framework.filter.properties;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/** Gateway Plugin Properties */
@Slf4j
@Getter
@Setter
@ToString
@Profile("!prod")
@Component
public class GatewayPluginProperties implements InitializingBean {
    public static final String GATEWAY_PLUGIN_PROPERTIES_PREFIX = "spring.plugin.config";

    /** Enable Or Disable Read Request Data 。 If true, all request body will cached */
    private Boolean readRequestData = false;

    /** Enable Or Disable Read Response Data */
    private Boolean readResponseData = false;

    /** Enable Or Disable Log Request Detail */
    private boolean logRequest = false;

    /** Hide header on log */
    private List<String> hideHeaderList = Collections.emptyList();

    private boolean logResponse = true;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!CollectionUtils.isEmpty(hideHeaderList)) {
            hideHeaderList = hideHeaderList.stream().map(String::toLowerCase).collect(Collectors.toList());
        }
    }
}
