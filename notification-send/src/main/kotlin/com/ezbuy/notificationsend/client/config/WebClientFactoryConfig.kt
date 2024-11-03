package com.ezbuy.notificationsend.client.config

import com.ezbuy.notificationsend.client.properties.AuthProperties
import com.reactify.client.WebClientFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager

@Configuration
class WebClientFactoryConfig(
    private val authProperties: AuthProperties
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
        factory.setWebClients(listOf(authProperties))
        return factory
    }
}
