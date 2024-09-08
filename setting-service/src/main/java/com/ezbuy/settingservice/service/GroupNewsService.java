/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
