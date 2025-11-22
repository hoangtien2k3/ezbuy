package com.ezbuy.settingservice.service;

import com.ezbuy.settingservice.model.dto.request.SearchGroupNewsRequest;
import com.ezbuy.settingservice.model.entity.GroupNews;
import com.ezbuy.settingservice.model.dto.request.CreateGroupNewsRequest;
import com.ezbuy.settingservice.model.dto.response.SearchGroupNewsResponse;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface GroupNewsService {
    Mono<DataResponse<GroupNews>> createGroupNews(CreateGroupNewsRequest request);

    Mono<DataResponse<GroupNews>> editGroupNews(String id, CreateGroupNewsRequest request);

    Mono<SearchGroupNewsResponse> findGroupNews(SearchGroupNewsRequest request);

    Mono<List<GroupNews>> getAllGroupNews();
}
