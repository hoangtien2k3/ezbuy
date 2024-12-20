package com.ezbuy.notificationsend.client.config

import com.ezbuy.notificationsend.client.properties.AuthProperties
import com.ezbuy.notificationsend.client.properties.OrderCpApiProperties
import com.ezbuy.notificationsend.client.properties.SettingClientProperties
import com.ezbuy.notificationsend.client.properties.SmsBrandNameProperties
import com.reactify.client.WebClientFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@Configuration
class WebClientFactoryConfig(
    private val authProperties: AuthProperties,
    private val smsBrandNameProperties: SmsBrandNameProperties,
    private val settingClientProperties: SettingClientProperties,
    private val orderCpApiProperties: OrderCpApiProperties
) {
    @Bean(name = ["webClientFactory"])
    fun webClientFactory(
        applicationContext: ApplicationContext,
        authorizedClientManager: ReactiveOAuth2AuthorizedClientManager
    ): WebClientFactory {
        val factory = WebClientFactory(
            applicationContext,
            authorizedClientManager
        )
        factory.webClients = listOf(
            authProperties,
            smsBrandNameProperties,
            settingClientProperties,
            orderCpApiProperties
        )
        return factory
    }
}
