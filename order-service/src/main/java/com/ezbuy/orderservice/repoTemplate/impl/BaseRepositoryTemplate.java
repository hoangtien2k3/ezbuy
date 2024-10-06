package com.ezbuy.orderservice.repoTemplate.impl;

import io.hoangtien2k3.reactify.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;

import java.util.Map;

public class BaseRepositoryTemplate {

    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    protected <T> Flux<T> listQuery(String sql, Map<String, Object> params, Class<T> type) {
        DatabaseClient.GenericExecuteSpec spec = entityTemplate.getDatabaseClient().sql(sql);
        if (!DataUtil.isNullOrEmpty(params)) {
            for (String param: params.keySet()) {
                spec = spec.bind(param, params.get(param));
            }
        }
        return spec.fetch()
                .all()
                .map(raw -> convert(raw, type));
    }

    protected <T> T convert(Map<String, Object> raw, Class<T> type) {
        return DataUtil.convertObject().convertValue(raw, type);
    }

}

