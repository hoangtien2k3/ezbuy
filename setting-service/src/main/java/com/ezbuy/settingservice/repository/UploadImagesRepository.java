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
package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.UploadImages;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UploadImagesRepository extends R2dbcRepository<UploadImages, String> {
    @Query("select * from upload_images u where u.status = 1 and u.parent_id = :parentId order by u.update_at desc")
    Flux<UploadImages> findAllByParentId(String parentId);

    @Query("select * from upload_images u where u.status = 1 and lower(u.name) like lower(:name)")
    Flux<UploadImages> findByName(String name);

    @Query("select * from upload_images u where u.parent_id = :parent_id and u.status = 1;")
    Mono<UploadImages> findByParentId(String parentId);

    @Query(
            "select * from upload_images u where u.status = 1 and ((:parentId is null and u.parent_id is null) or (u.parent_id = :parentId)) and lower(u.name) like lower(:name)")
    Flux<UploadImages> findByParentIdAndName(String parentId, String name);

    @Query("select * from upload_images u where u.status = 1 and u.type = 2")
    Flux<UploadImages> findAllFolder();
}
