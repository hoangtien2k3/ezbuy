package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.MarketInfo;
import java.util.List;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketInfoRepository extends R2dbcRepository<MarketInfo, UUID> {
    @Query(value = "select * from market_info where id = :id")
    Mono<MarketInfo> getById(String id);

    @Query("select * from market_info order by create_at desc")
    Flux<MarketInfo> findAllMarketInfo();

    @Query(
            "select * from market_info where service_id =:telecomServiceId and (service_alias =:serviceAlias or :serviceAlias is null)")
    Flux<MarketInfo> findByServiceId(String telecomServiceId, String serviceAlias);

    Flux<MarketInfo> findByServiceAlias(String serviceAlias);

    @Query(
            value = "update market_info " + "set service_id       = :serviceId, " + "    title            = :title, "
                    + "    navigator_url    = :navigatorUrl, " + "    market_order     = :marketOrder, "
                    + "    market_image_url = :marketImageUrl, " + "    status           = :status, "
                    + "    update_by        = :user, " + "    update_at        = CURRENT_TIMESTAMP(), "
                    + "    service_alias = :serviceAlias " + "where id = :id")
    Mono<MarketInfo> updateMarketInfo(
            String serviceId,
            String title,
            String navigatorUrl,
            Integer marketOrder,
            String marketImageUrl,
            Integer status,
            String user,
            String id,
            String serviceAlias);

    @Query(value = "select * from market_info where market_order = :marketOrder")
    Mono<MarketInfo> findByMarketOrder(Integer marketOrder);

    @Query("select * from market_info where status = 1")
    Flux<MarketInfo> getAllActiveMarketInfo();

    @Query(value = "select * from market_info where service_id in (:lstServiceId) and status = 1 ")
    Flux<MarketInfo> getByServiceId(List<String> lstServiceId);

    @Query(value = "select * from market_info where service_alias in (:lstAlias) and status = 1 ")
    Flux<MarketInfo> getByServiceAlias(List<String> lstAlias);
}
