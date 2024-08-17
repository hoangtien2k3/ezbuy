package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.dto.ContentDisplayDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.request.ComponentPageRequest;
import com.ezbuy.settingmodel.response.SearchingComponentResponse;
import com.ezbuy.settingservice.repositoryTemplate.ContentDisplayRepositoryTemplate;
import com.ezbuy.settingservice.service.ContentDisplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentDisplayServiceImpl extends BaseServiceHandler implements ContentDisplayService {
    private final ContentDisplayRepositoryTemplate contentDisplayRepositoryTemplate;

    @Override
    public Mono<SearchingComponentResponse> searchOriginComponents(ComponentPageRequest request) {
        //validate request
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);

        Flux<ContentDisplayDTO> components = contentDisplayRepositoryTemplate.getOriginComponent(request);
        Mono<Long> countMono = contentDisplayRepositoryTemplate.countComponents(request);
        return Mono.zip(components.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            SearchingComponentResponse response = new SearchingComponentResponse();
            response.setContentDisplays(zip.getT1());
            response.setPagination(pagination);

            return response;
        });
    }

    @Override
    public Mono<ContentDisplayDTO> getDetails(String id) {
        return contentDisplayRepositoryTemplate.getContentWithParentId(id);
    }

    @Override
    public Mono<List<ContentDisplayDTO>> getOriginComponentDetails(String name) {
        return contentDisplayRepositoryTemplate.getOriginComponentDetails(name);
    }
}
