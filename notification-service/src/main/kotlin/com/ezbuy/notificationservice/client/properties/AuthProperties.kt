package com.ezbuy.notificationservice.client.properties

import io.hoangtien2k3.reactify.client.properties.WebClientProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import lombok.AllArgsConstructor

@Component("authProperties")
@ConfigurationProperties(prefix = "client.auth", ignoreInvalidFields = true)
@AllArgsConstructor
class AuthProperties : WebClientProperties()
