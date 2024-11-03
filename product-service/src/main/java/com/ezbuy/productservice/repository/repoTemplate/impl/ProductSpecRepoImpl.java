package com.ezbuy.productservice.repository.repoTemplate.impl;

import com.ezbuy.productmodel.dto.ProductSpecCharAndValDTO;
import com.ezbuy.productmodel.dto.ProductSpecCharValueDTO;
import com.ezbuy.productmodel.model.ProductSpecCharAndValue;
import com.ezbuy.productservice.repository.repoTemplate.ProductSpecRepo;
import com.reactify.util.DataUtil;
import io.r2dbc.spi.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class ProductSpecRepoImpl implements ProductSpecRepo {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<ProductSpecCharAndValue> findByTelecomServiceAliasIncludeValue(String telecomServiceAlis) {
        return template.getDatabaseClient()
                .sql("SELECT c.id id, c.code code, c.name name, c.telecom_service_id telecomServiceId, "
                        + " c.telecom_service_alias telecomServiceAlias, c.status status, c.display_order displayOrder,c.view_type viewType, "
                        + " v.id valId, v.product_spec_char_id valProductSpecCharId, v.value valValue, v.name valName, "
                        + " v.status valStatus, v.display_order valDisplayOrder " + " FROM product_spec_char c "
                        + " join product_spec_char_value v on c.id = v.product_spec_char_id "
                        + " WHERE c.telecom_service_alias = :alias AND c.status = 1 "
                        + " AND v.status = 1 and c.state = 1 and v.state = 1 " + " ORDER BY name, valName")
                .bind("alias", telecomServiceAlis)
                .map((row) -> ProductSpecCharAndValue.builder()
                        .id(DataUtil.safeToString(row.get("id")))
                        .name(DataUtil.safeToString(row.get("name")))
                        .code(DataUtil.safeToString(row.get("code")))
                        .telecomServiceId(DataUtil.safeToString(row.get("telecomServiceId")))
                        .telecomServiceAlias(DataUtil.safeToString(row.get("telecomServiceAlias")))
                        .orderDisplay(DataUtil.safeToInt(row.get("displayOrder")))
                        .viewType(DataUtil.safeToString(row.get("viewType")))
                        .valId(UUID.fromString(DataUtil.safeToString(row.get("valId"))))
                        .valProductSpecCharId(UUID.fromString(DataUtil.safeToString(row.get("valProductSpecCharId"))))
                        .valValue(DataUtil.safeToString(row.get("valValue")))
                        .valName(DataUtil.safeToString(row.get("valName")))
                        .valOrderDisplay(DataUtil.safeToInt(row.get("valDisplayOrder")))
                        .build())
                .all();
    }

    @Override
    public Flux<ProductSpecCharAndValDTO> findAllActive() {
        return template.getDatabaseClient()
                .sql("SELECT c.id id, c.code code, c.name name, c.telecom_service_id telecomServiceId, "
                        + " c.telecom_service_alias telecomServiceAlias, c.status status, c.display_order displayOrder,"
                        + " v.id valId, v.product_spec_char_id valProductSpecCharId, v.value valValue, v.name valName, "
                        + " v.status valStatus, v.display_order valDisplayOrder " + " FROM product_spec_char c "
                        + " left join product_spec_char_value v on c.id = v.product_spec_char_id and v.status = 1 "
                        + " WHERE c.status = 1" + " ORDER BY name, valName")
                .map(row -> {
                    ProductSpecCharValueDTO valueDTO = ProductSpecCharValueDTO.builder()
                            .id(DataUtil.safeToString(row.get("valId")))
                            .productSpecCharId(DataUtil.safeToString(row.get("valProductSpecCharId")))
                            .value(DataUtil.safeToString(row.get("valValue")))
                            .name(DataUtil.safeToString(row.get("valName")))
                            .displayOrder(DataUtil.safeToInt(row.get("valDisplayOrder")))
                            .build();
                    List<ProductSpecCharValueDTO> values = new ArrayList<>();
                    values.add(valueDTO);

                    return ProductSpecCharAndValDTO.builder()
                            .id(DataUtil.safeToString(row.get("id")))
                            .code(DataUtil.safeToString(row.get("code")))
                            .name(DataUtil.safeToString(row.get("name")))
                            .telecomServiceId(DataUtil.safeToString(row.get("telecomServiceId")))
                            .telecomServiceAlias(DataUtil.safeToString(row.get("telecomServiceAlias")))
                            .displayOrder(DataUtil.safeToInt(row.get("displayOrder")))
                            .productSpecCharValueDTOList(values)
                            .build();
                })
                .all();
    }

    @Override
    public Flux<Object> updateBatchProductSpecCharAndVal(List<ProductSpecCharAndValDTO> productSpecCharAndValList) {
        String updateQuery = "update product_spec_char set  name = ? " + " where id = ?";
        return template.getDatabaseClient().inConnectionMany(connection -> {
            Statement statement = connection.createStatement(updateQuery);
            productSpecCharAndValList.forEach(productSpecValue -> statement
                    .bind(0, productSpecValue.getName())
                    .bind(1, productSpecValue.getId())
                    .add());
            return Flux.from(statement.execute()).flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
        });
    }

    @Override
    public Flux<Object> updateBatchProductSpecCharValue(List<ProductSpecCharValueDTO> productSpecCharValueList) {
        String updateQuery = "update product_spec_char_value set name = ? " + " where id = ?";
        return template.getDatabaseClient().inConnectionMany(connection -> {
            Statement statement = connection.createStatement(updateQuery);
            productSpecCharValueList.forEach(productSpecValue -> statement
                    .bind(0, productSpecValue.getName())
                    .bind(1, productSpecValue.getId())
                    .add());
            return Flux.from(statement.execute()).flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
        });
    }

    @Override
    public Flux<Object> deleteBatchProductSpecCharValue(List<ProductSpecCharValueDTO> productSpecCharValueList) {
        String updateQuery = "update product_spec_char_value  set status = 0" + " where id = ?";
        return template.getDatabaseClient().inConnectionMany(connection -> {
            Statement statement = connection.createStatement(updateQuery);
            productSpecCharValueList.forEach(productSpecValue ->
                    statement.bind(0, productSpecValue.getId()).add());
            return Flux.from(statement.execute()).flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
        });
    }

    @Override
    public Flux<Object> deleteBatchProductSpecCharAndVal(List<ProductSpecCharAndValDTO> productSpecCharAndValList) {
        String updateQuery = "update product_spec_char  set status = 0" + " where id = ?";
        return template.getDatabaseClient().inConnectionMany(connection -> {
            Statement statement = connection.createStatement(updateQuery);
            productSpecCharAndValList.forEach(productSpecValue ->
                    statement.bind(0, productSpecValue.getId()).add());
            return Flux.from(statement.execute()).flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
        });
    }

    @Override
    public Flux<Object> updateProductSpecCharValue(List<ProductSpecCharValueDTO> productSpecCharValueList) {
        String updateQuery = "update product_spec_char_value set state = ?, display_order = ?" + " where id = ?";
        return template.getDatabaseClient().inConnectionMany(connection -> {
            Statement statement = connection.createStatement(updateQuery);
            productSpecCharValueList.forEach(productSpecValue -> statement
                    .bind(0, productSpecValue.getState())
                    .bind(1, productSpecValue.getDisplayOrder())
                    .bind(2, productSpecValue.getId())
                    .add());
            return Flux.from(statement.execute()).flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
        });
    }

    @Override
    public Flux<Object> updateProductSpecCharValueNew(List<ProductSpecCharValueDTO> productSpecCharValueList) {
        if (productSpecCharValueList.isEmpty()) {
            return Flux.empty();
        }
        String updateQuery =
                "update product_spec_char_value set status = ?, display_order = ?,name=?,value=?" + " where id = ?";
        return template.getDatabaseClient().inConnectionMany(connection -> {
            Statement statement = connection.createStatement(updateQuery);
            productSpecCharValueList.forEach(productSpecValue -> statement
                    .bind(0, productSpecValue.getStatus())
                    .bind(1, productSpecValue.getDisplayOrder())
                    .bind(2, productSpecValue.getName())
                    .bind(3, productSpecValue.getValue())
                    .bind(4, productSpecValue.getId())
                    .add());
            return Flux.from(statement.execute()).flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
        });
    }

    @Override
    public Flux<Object> updateProductSpecChar(List<ProductSpecCharAndValDTO> productSpecCharAndValDTOS) {
        String updateQuery = "update product_spec_char set  state = ?, display_order = ?" + " where id = ?";
        return template.getDatabaseClient().inConnectionMany(connection -> {
            Statement statement = connection.createStatement(updateQuery);
            productSpecCharAndValDTOS.forEach(productSpecValue -> statement
                    .bind(0, productSpecValue.getState())
                    .bind(1, productSpecValue.getDisplayOrder())
                    .bind(2, productSpecValue.getId())
                    .add());
            return Flux.from(statement.execute()).flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
        });
    }

    @Override
    public Flux<Object> updateProductSpecCharNew(List<ProductSpecCharAndValDTO> productSpecCharAndValDTOS) {
        String updateQuery = "update product_spec_char set  status = ?, display_order = ?,view_type=?,name=?,code=?"
                + " where id = ?";
        return template.getDatabaseClient().inConnectionMany(connection -> {
            Statement statement = connection.createStatement(updateQuery);
            productSpecCharAndValDTOS.forEach(productSpecValue -> statement
                    .bind(0, productSpecValue.getStatus())
                    .bind(1, productSpecValue.getDisplayOrder())
                    .bind(2, productSpecValue.getViewType())
                    .bind(3, productSpecValue.getName())
                    .bind(4, productSpecValue.getCode())
                    .bind(5, productSpecValue.getId())
                    .add());
            return Flux.from(statement.execute()).flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
        });
    }

    @Override
    public Flux<Object> deleteProductSpecChar(String productSpecCharId) {
        String deleteQuery = "update product_spec_char set status = 0 where id = ?";
        return template.getDatabaseClient().inConnectionMany(connection -> {
            Statement statement = connection.createStatement(deleteQuery);
            statement.bind(0, productSpecCharId).add();
            return Flux.from(statement.execute()).flatMap(rs -> rs.map((row, map) -> row.get("id", String.class)));
        });
    }
}
