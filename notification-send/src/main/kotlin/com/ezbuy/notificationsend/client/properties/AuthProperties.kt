package com.ezbuy.notificationsend.client.properties

import com.reactify.client.properties.WebClientProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component("authProperties")
@ConfigurationProperties(prefix = "client.auth", ignoreInvalidFields = true)
class AuthProperties : WebClientProperties()