package com.ezbuy.orderservice.repository.query;

public interface OrderFieldConfigQuery {

    String queryFindConfigByOrderTypeAndTelecomServiceIds = "select ofc.* from order_field_config ofc" +
            " join order_type o on ofc.order_type_id = o.id" +
            " where o.alias = :orderType and o.status = 1" +
            " and ofc.service_alias in (:lstAlias)" +
            " and ofc.status = 1";
}
