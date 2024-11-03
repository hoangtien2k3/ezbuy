package com.ezbuy.productservice.repository.repoTemplate.impl;

import com.ezbuy.productmodel.dto.TelecomServiceResponse;
import com.ezbuy.productmodel.response.MegaMenuResponse;
import com.ezbuy.productservice.repository.repoTemplate.TelecomServiceRepository;
import com.reactify.util.DataUtil;
import lombok.Data;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Data
@Repository
public class TelecomServiceRepositoryImpl implements TelecomServiceRepository {
    private final R2dbcEntityTemplate template;

    @Override
    public Flux<MegaMenuResponse> getProductsForMegaMenu() {
        return template.getDatabaseClient()
                .sql(
                        "select sg.image as groupImage, sg.name as groupName, ts.name as productName, ts.description as productDescription, ts.landing_page_url as productLink from telecom_service ts inner join service_group sg on ts.group_id = sg.id")
                .map((row) -> MegaMenuResponse.builder()
                        .groupImage(DataUtil.safeToString(row.get("groupImage")))
                        .groupName(DataUtil.safeToString(row.get("groupName")))
                        .productLink(DataUtil.safeToString(row.get("productLink")))
                        .productName(DataUtil.safeToString(row.get("productName")))
                        .productDescription(DataUtil.safeToString(row.get("productDescription")))
                        .build())
                .all();
    }

    @Override
    public Flux<TelecomServiceResponse> getTelecomServices() {
        String query = "SELECT distinct ts.id, ts.name, ts.client_id, ts.service_alias \n"
                + "FROM sme_product.active_telecom at right join sme_product.telecom_service ts \n"
                + "on at.telecom_service_id = ts.id \n"
                + "where ts.status = 1 and (at.id is not null or ts.is_default = 1)";
        return template.getDatabaseClient().sql(query).fetch().all().map(stringObjectMap -> {
            TelecomServiceResponse result = new TelecomServiceResponse();
            result.setTelecomServiceId(DataUtil.safeToString(stringObjectMap.get("id")));
            result.setClientId(DataUtil.safeToString(stringObjectMap.get("client_id")));
            result.setName(DataUtil.safeToString(stringObjectMap.get("name")));
            result.setServiceAlias(DataUtil.safeToString(stringObjectMap.get("service_alias")));
            result.setTelecomServiceAlias(DataUtil.safeToString(stringObjectMap.get("service_alias")));
            return result;
        });
    }
}
