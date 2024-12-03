package com.ezbuy.notificationservice.repository.repoTemplate.impl

import com.ezbuy.notificationmodel.dto.UserTransmissionDTO
import com.ezbuy.notificationservice.repository.query.TransmissionQuery
import com.ezbuy.notificationservice.repository.repoTemplate.BaseRepositoryTemplate
import com.ezbuy.notificationservice.repository.repoTemplate.TransmissionRepoTemplate
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.reactify.util.DataUtil
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
class TransmissionRepoTemplateImpl(
    private val template: R2dbcEntityTemplate
) : TransmissionRepoTemplate, BaseRepositoryTemplate(template) {

    private val objectMapper: ObjectMapper = JsonMapper.builder()
        .addModule(JavaTimeModule())
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .build()

    override fun searchUserTransmission(
        email: String?,
        templateMail: String?,
        from: LocalDateTime?,
        to: LocalDateTime?,
        offset: Int,
        limit: Int,
        sort: String
    ): Flux<UserTransmissionDTO> {
        val sb = StringBuilder(TransmissionQuery.findAllUserTransmissionFromTo)

        if (!DataUtil.isNullOrEmpty(email)) {
            sb.append(" AND t.email = :email ")
        }
        if (!DataUtil.isNullOrEmpty(templateMail)) {
            sb.append(" AND c.template_mail = :templateMail ")
        }
        from?.let { sb.append(" AND t.create_at >= :from ") }
        to?.let { sb.append(" AND t.create_at <= :to ") }
        sb.append(sort).append(" LIMIT :limit OFFSET :offset")

        val params = mutableMapOf<String, Any>()
        params["offset"] = offset
        params["limit"] = limit
        email?.takeIf { it.isNotBlank() }?.let { params["email"] = it }
        templateMail?.takeIf { it.isNotBlank() }?.let { params["templateMail"] = it }
        from?.let { params["from"] = from }
        to?.let { params["to"] = to }

        return listQuery(sb.toString(), params, UserTransmissionDTO::class.java)
    }

    override fun countUserTransmission(
        email: String?,
        templateMail: String?,
        from: LocalDateTime?,
        to: LocalDateTime?
    ): Mono<Long> {
        val sb = StringBuilder(TransmissionQuery.findCountUserTransmissionFromTo)

        if (!DataUtil.isNullOrEmpty(email)) {
            sb.append(" AND t.email = :email ")
        }
        if (!DataUtil.isNullOrEmpty(templateMail)) {
            sb.append(" AND c.template_mail = :templateMail ")
        }
        from?.let { sb.append(" AND t.create_at >= :from ") }
        to?.let { sb.append(" AND t.create_at <= :to ") }

        val params = mutableMapOf<String, Any>()
        from?.let { params["from"] = from }
        to?.let { params["to"] = to }
        email?.takeIf { it.isNotBlank() }?.let { params["email"] = it }
        templateMail?.takeIf { it.isNotBlank() }?.let { params["templateMail"] = it }

        return countQuery(sb.toString(), params)
    }
}
