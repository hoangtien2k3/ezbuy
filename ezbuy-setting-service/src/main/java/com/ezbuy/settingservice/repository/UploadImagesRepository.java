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

    @Query("select * from upload_images u where u.id = :id and u.status = 1;")
    Mono<UploadImages> findById(String id);

    @Query(
            "select * from upload_images u where u.status = 1 and ((:parentId is null and u.parent_id is null) or (u.parent_id = :parentId)) and lower(u.name) like lower(:name)")
    Flux<UploadImages> findByParentIdAndName(String parentId, String name);

    @Query("select * from upload_images u where u.status = 1 and u.type = 2")
    Flux<UploadImages> findAllFolder();
}
