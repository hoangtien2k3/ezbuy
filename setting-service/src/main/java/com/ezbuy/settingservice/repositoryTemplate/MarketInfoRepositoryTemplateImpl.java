package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.MarketInfoDTO;
import com.ezbuy.settingmodel.model.MarketInfo;
import com.ezbuy.settingmodel.request.SearchMarketInfoRequest;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.SQLUtils;
import io.hoangtien2k3.reactify.SortingUtils;
import io.r2dbc.spi.Row;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketInfoRepositoryTemplateImpl implements MarketInfoRepositoryTemplate {
    private final R2dbcEntityTemplate template;

    @Override
    public Flux<MarketInfoDTO> queryList(SearchMarketInfoRequest request) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQuery(query, params, request);
        query.append("limit :offset,:limit");
        params.put("limit", request.getPageSize());
        params.put("offset", (request.getPageIndex() - 1) * request.getPageSize());

        DatabaseClient.GenericExecuteSpec exeSpec = template.getDatabaseClient().sql(query.toString());
        for (String key : params.keySet()) {
            exeSpec = exeSpec.bind(key, params.get(key));
        }
        return exeSpec.map((a, b) -> convertRow(a)).all();
    }

    @Override
    public Mono<Long> count(SearchMarketInfoRequest request) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQuery(query, params, request);
        query.insert(0, "select count(1) from (");
        query.append(") repo_count_alias");
        DatabaseClient.GenericExecuteSpec exeSpec = template.getDatabaseClient().sql(query.toString());
        for (String key : params.keySet()) {
            exeSpec = exeSpec.bind(key, params.get(key));
        }
        return exeSpec.map(row -> row.get(0)).first().cast(Long.class);
    }

    private void buildQuery(StringBuilder builder, Map<String, Object> params, SearchMarketInfoRequest request) {
        builder.append("select mi.*, count(mi.id), ts.name from market_info mi \n");
        builder.append("inner join telecom_service ts on ts.origin_id = mi.service_id  \n");
        builder.append("where ts.status = 1 \n");

        if (!DataUtil.isNullOrEmpty(request.getFromDate())) {
            builder.append("and mi.create_at >= :from \n");
            params.put("from", request.getFromDate().atStartOfDay());
        }

        if (!DataUtil.isNullOrEmpty(request.getToDate())) {
            builder.append("and mi.create_at < :to \n");
            params.put("to", request.getToDate().plusDays(1).atStartOfDay());
        }

        if (!DataUtil.isNullOrEmpty(request.getTitle())) {
            builder.append("and lower(mi.title) like concat('%', :title, '%') \n");
            params.put(
                    "title",
                    SQLUtils.replaceSpecialDigit(request.getTitle().trim().toLowerCase()));
        }

        if (!DataUtil.isNullOrEmpty(request.getServiceId())) {
            builder.append("and mi.service_id = :serviceId \n");
            params.put("serviceId", request.getServiceId());
        }

        // bo sung them dieu kien filter cho serviceAlias PYCXXX/LuongToanTrinhScontract
        if (!DataUtil.isNullOrEmpty(request.getServiceAlias())) {
            builder.append("and mi.service_alias = :serviceAlias \n");
            params.put("serviceAlias", request.getServiceAlias());
        }

        builder.append("group by ts.origin_id, mi.create_at, mi.id \n");

        builder.append("order by ");
        String otherSort = DataUtil.safeToString(request.getSort(), "+createAt");
        builder.append(SortingUtils.parseSorting("-type," + otherSort, MarketInfo.class));
        builder.append("\n");
    }

    private MarketInfoDTO convertRow(Row row) {
        MarketInfoDTO marketInfoDTO = MarketInfoDTO.builder()
                .id(DataUtil.safeToString(row.get("id")))
                .serviceId(DataUtil.safeToString(row.get("service_id")))
                .title(DataUtil.safeToString(row.get("title")))
                .navigatorUrl(DataUtil.safeToString(row.get("navigator_url")))
                .marketOrder(DataUtil.safeToInt(row.get("market_order")))
                .marketImageUrl(DataUtil.safeToString(row.get("market_image_url")))
                .serviceAlias(DataUtil.safeToString(row.get("service_alias")))
                .status(DataUtil.safeToInt(row.get("status")))
                .updateAt((LocalDateTime) row.get("update_at"))
                .createAt((LocalDateTime) row.get("create_at"))
                .createBy(DataUtil.safeToString(row.get("create_by")))
                .updateBy(DataUtil.safeToString(row.get("update_by")))
                .build();
        marketInfoDTO.setNameService(DataUtil.safeToString(row.get("name")));
        return marketInfoDTO;
    }
}
