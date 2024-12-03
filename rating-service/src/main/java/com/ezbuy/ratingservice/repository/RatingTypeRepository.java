package com.ezbuy.ratingservice.repository;

import com.ezbuy.ratingmodel.model.RatingType;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingTypeRepository extends R2dbcRepository<RatingType, String> {

    // Lấy tất cả các rating_type đang hoạt động (status = 1), sắp xếp theo tên
    @Query("SELECT * FROM rating_type WHERE status = 1 ORDER BY name")
    Flux<RatingType> getAllActive();

    // Lấy thời gian hệ thống hiện tại
    @Query("SELECT current_timestamp")
    Mono<LocalDateTime> getSysDate();
}
