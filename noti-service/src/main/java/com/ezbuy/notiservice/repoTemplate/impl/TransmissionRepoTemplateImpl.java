package com.ezbuy.notiservice.repoTemplate.impl;

import com.ezbuy.notimodel.dto.UserTransmissionDTO;
import com.ezbuy.notiservice.repoTemplate.TransmissionRepoTemplate;
import com.ezbuy.notiservice.repository.query.TransmissionQuery;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reactify.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class TransmissionRepoTemplateImpl implements TransmissionRepoTemplate {

	private final R2dbcEntityTemplate template;
	private final ObjectMapper objectMapper = JsonMapper.builder()
			.addModule(new JavaTimeModule())
			.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build();
	@Override
	public Flux<UserTransmissionDTO> searchUserTransmission(String email, String templateMail, LocalDateTime from, LocalDateTime to, int offset, int limit, String sort) {
		StringBuilder sb = new StringBuilder(TransmissionQuery.FIND_ALL_USER_TRANSMISSION_FROM_TO);
		if (!DataUtil.isNullOrEmpty(email)) {
			sb.append(" and t.email = :email ");
		}
		if (!DataUtil.isNullOrEmpty(templateMail)) {
			sb.append(" and c.template_mail = :templateMail ");
		}
		if (from != null) {
			sb.append(" and t.create_at >= :from ");
		}
		if (to != null) {
			sb.append(" and t.create_at <= :to ");
		}
		sb.append(sort).append(" limit :limit offset :offset");
		DatabaseClient.GenericExecuteSpec genericExecuteSpec = template.getDatabaseClient()
				.sql(sb.toString())
				.bind("offset", offset)
				.bind("limit", limit);

		if (!DataUtil.isNullOrEmpty(email)) {
			genericExecuteSpec = genericExecuteSpec.bind("email", email);
		}
		if (!DataUtil.isNullOrEmpty(templateMail)) {
			genericExecuteSpec = genericExecuteSpec.bind("templateMail", templateMail);
		}
		if (from != null) {
			genericExecuteSpec = genericExecuteSpec.bind("from", from);
		}
		if (to != null) {
			genericExecuteSpec = genericExecuteSpec.bind("to", to);
		}
		return genericExecuteSpec
				.fetch()
				.all()
				.map(raw -> convert(raw, UserTransmissionDTO.class));
	}

	protected <T> T convert(Map<String, Object> raw, Class<T> type) {
		return objectMapper.convertValue(raw, type);
	}

	@Override
	public Mono<Long> countUserTransmission(String email, String templateMail, LocalDateTime from, LocalDateTime to) {
		StringBuilder sb = new StringBuilder(" select count(*)  as totalCount from (select t.id ,t.email, t.create_at, t.create_by, t.state, c.template_mail \n");
		sb.append(" from sme_notification.transmission t\n");
		sb.append(" inner join sme_notification.notification n on t.notification_id = n.id\n");
		sb.append(" inner join sme_notification.notification_content c on n.notification_content_id = c.id\n");
		sb.append(" where 1=1 ");
		if (!DataUtil.isNullOrEmpty(email)) {
			sb.append(" and t.email = :email ");
		}
		if (!DataUtil.isNullOrEmpty(templateMail)) {
			sb.append(" and c.template_mail = :templateMail ");
		}
		if (from != null) {
			sb.append(" and t.create_at >= :from ");
		}
		if (to != null) {
			sb.append(" and t.create_at <= :to ");
		}
		sb.append(") as tnc ");
		DatabaseClient.GenericExecuteSpec genericExecuteSpec = template.getDatabaseClient()
				.sql(sb.toString());
		if (from != null) {
			genericExecuteSpec = genericExecuteSpec.bind("from", from);
		}
		if (to != null) {
			genericExecuteSpec = genericExecuteSpec.bind("to", to);
		}
		if (!DataUtil.isNullOrEmpty(email)) {
			genericExecuteSpec = genericExecuteSpec.bind("email", email);
		}
		if (!DataUtil.isNullOrEmpty(templateMail)) {
			genericExecuteSpec = genericExecuteSpec.bind("templateMail", templateMail);
		}

		return genericExecuteSpec.fetch()
				.first()
				.map(result -> result.get("totalCount"))
				.cast(Long.class);
	}
}
