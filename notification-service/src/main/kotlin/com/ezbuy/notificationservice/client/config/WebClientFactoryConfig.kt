package com.ezbuy.notificationservice.client.config

import com.ezbuy.notificationservice.client.properties.AuthProperties
import com.reactify.client.WebClientFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebClientFactoryConfig(private val authProperties: AuthProperties) {
    @Bean(name = ["webClientFactory"])
    fun webClientFactory(): WebClientFactory {
        return WebClientFactory(listOf(authProperties))
    }
}
