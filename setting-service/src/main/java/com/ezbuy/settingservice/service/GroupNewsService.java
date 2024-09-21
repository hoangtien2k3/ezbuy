package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.request.SearchGroupNewsRequest;
import com.ezbuy.settingmodel.model.GroupNews;
import com.ezbuy.settingmodel.request.CreateGroupNewsRequest;
import com.ezbuy.settingmodel.response.SearchGroupNewsResponse;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface GroupNewsService {
    Mono<DataResponse<GroupNews>> createGroupNews(CreateGroupNewsRequest request);

    Mono<DataResponse<GroupNews>> editGroupNews(String id, CreateGroupNewsRequest request);

    Mono<SearchGroupNewsResponse> findGroupNews(SearchGroupNewsRequest request);

    Mono<List<GroupNews>> getAllGroupNews();
}
