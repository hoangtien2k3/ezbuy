package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.ContentDisplayDTO;
import com.ezbuy.settingservice.model.entity.ContentDisplay;
import com.ezbuy.settingservice.model.dto.request.ComponentPageRequest;
import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.repository.BaseTemplateRepository;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SQLUtils;
import com.ezbuy.core.util.SortingUtils;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ContentDisplayRepositoryTemplateImpl extends BaseTemplateRepository
        implements ContentDisplayRepositoryTemplate {
    @Override
    public Mono<List<ContentDisplayDTO>> getAllByPageId(String pageId) {
        Map<String, Object> params = new HashMap<>();
        String query =
                """
        with recursive n as (
              select *\s
          from content_display where page_id = :pageId and status = 1 and parent_id is null
        union all
        select c.*
            from n
            join content_display c on c.parent_id = n.id and c.status = 1
            )
            select * FROM n
        """;
        params.put("pageId", pageId);
        return listQuery(query, params, ContentDisplayDTO.class).collectList().map(this::getParentContents);
    }

    private List<ContentDisplayDTO> getParentContents(List<ContentDisplayDTO> displayDTOS) {
        displayDTOS.sort((el1, el2) -> {
            int index1 = DataUtil.safeToInt(el1.getDisplayOrder());
            int index2 = DataUtil.safeToInt(el2.getDisplayOrder());
            return Integer.compare(index1, index2);
        });
        List<ContentDisplayDTO> response = new ArrayList<>();
        for (ContentDisplayDTO contentDisplay : displayDTOS) {
            List<ContentDisplayDTO> children = getChildren(contentDisplay.getId(), displayDTOS);
            contentDisplay.setContentDisplayDTOList(children);
            if (Objects.isNull(contentDisplay.getParentId())) {
                response.add(contentDisplay);
            }
        }
        return response;
    }

    private List<ContentDisplayDTO> getChildren(String parentId, List<ContentDisplayDTO> displayDTOS) {
        return displayDTOS.stream()
                .filter(p -> Objects.equals(p.getParentId(), parentId))
                .collect(Collectors.toList());
    }

    @Override
    public Flux<ContentDisplayDTO> getOriginComponent(ComponentPageRequest request) {
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " name \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), ContentDisplay.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryComponents(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n").append(" LIMIT :pageSize  \n" + "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, ContentDisplayDTO.class);
    }

    @Override
    public Mono<Long> countComponents(ComponentPageRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryComponents(builder, params, request);

        return countQuery(builder.toString(), params);
    }

    @Override
    public Mono<ContentDisplayDTO> getContentWithParentId(String id) {
        Map<String, Object> params = new HashMap<>();
        String query =
                """
        with recursive n as (
              select *\s
          from content_display where id = :id and status = 1
        union all
        select c.*
            from n
            join content_display c on c.parent_id = n.id and c.status = 1\s
            )
            select * FROM n order by display_order
        """;
        params.put("id", id);
        return listQuery(query, params, ContentDisplayDTO.class)
                .switchIfEmpty(
                        Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "content-display.not.found")))
                .collectList()
                .flatMap(map -> {
                    // fix case null cua code cu
                    List<ContentDisplayDTO> contentDisplayDTOList = getParentContents(map);
                    return (CollectionUtils.isEmpty(contentDisplayDTOList))
                            ? Mono.just(new ContentDisplayDTO())
                            : Mono.just(contentDisplayDTOList.getFirst());
                });
    }

    @Override
    public Mono<List<ContentDisplayDTO>> getOriginComponentDetails(String name) {
        Map<String, Object> params = new HashMap<>();
        String query =
                """
        with recursive n as (
              select *\s
          from content_display \
        where id in (select cd.id from content_display cd where cd.parent_id is null and cd.status = 1 and cd.is_original = 1 and cd.name like CONCAT('%',:name, '%') )  \
        and status = 1
        union all
        select c.*
            from n
            join content_display c on c.parent_id = n.id and c.status =1\s
            )
            select * FROM n order by name
        """;
        params.put("name", SQLUtils.replaceSpecialDigit(name));
        return listQuery(query, params, ContentDisplayDTO.class).collectList().map(this::getParentContents);
    }

    @Override
    public Flux<ContentDisplay> getOldContents(String pageId) {
        Map<String, Object> params = new HashMap<>();
        String query =
                """
        with recursive n as (
              select *\s
          from content_display where id IN (select pc.component_id from page_component pc where pc.page_id = :pageId )
        union all
        select c.*
            from n
            join content_display c on c.parent_id = n.id
            )
            select * FROM n
        """;
        params.put("pageId", pageId);
        return listQuery(query, params, ContentDisplay.class);
    }

    private void buildQueryComponents(StringBuilder builder, Map<String, Object> params, ComponentPageRequest request) {
        builder.append("select * \n")
                .append("from sme_setting.content_display u \n")
                .append("where 1=1 and u.status = 1 and u.is_original = 1 and u.parent_id is null \n");
        if (!DataUtil.isNullOrEmpty(request.getName())) {
            builder.append("and u.name like CONCAT('%',:name, '%') \n");
            params.put("name", SQLUtils.replaceSpecialDigit(request.getName()));
        }
    }
}
