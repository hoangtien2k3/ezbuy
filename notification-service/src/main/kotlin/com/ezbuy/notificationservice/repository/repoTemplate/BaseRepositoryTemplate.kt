package com.ezbuy.notificationservice.repository.repoTemplate

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.reactify.util.DataUtil
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

open class BaseRepositoryTemplate(
    private var entityTemplate: R2dbcEntityTemplate
) {
    private val objectMapper: ObjectMapper = JsonMapper.builder()
        .addModule(JavaTimeModule())
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .build()

    protected fun <T : Any> listQuery(sql: String, params: Map<String, Any>, type: Class<T>): Flux<T> {
        var spec = entityTemplate.databaseClient.sql(sql)
        if (!DataUtil.isNullOrEmpty(params)) {
            params.forEach { (key, value) ->
                spec = spec.bind(key, value)
            }
        }
        return spec.fetch().all().map { raw -> convert(raw, type) }
    }

    protected fun countQuery(sql: String, params: Map<String, Any>): Mono<Long> {
        val query = "select count(*) as totalCount from ($sql) as tmp"
        var spec = entityTemplate.databaseClient.sql(query)
        if (!DataUtil.isNullOrEmpty(params)) {
            params.forEach { (key, value) ->
                spec = spec.bind(key, value)
            }
        }
        return spec.fetch().first().map { result -> result["totalCount"] as Long }
    }

    protected fun <T> convert(raw: Map<String, Any>, type: Class<T>): T {
        return objectMapper.convertValue(raw, type)
    }
}