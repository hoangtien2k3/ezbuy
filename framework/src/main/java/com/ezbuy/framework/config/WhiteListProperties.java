package com.ezbuy.framework.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ezbuy.framework.model.WhiteList;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "application")
public class WhiteListProperties {
    private List<WhiteList> whiteList;
}
