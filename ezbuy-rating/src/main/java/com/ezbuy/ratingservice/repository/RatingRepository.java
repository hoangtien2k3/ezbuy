package com.ezbuy.ratingservice.repository;

import com.ezbuy.ratingservice.model.entity.Rating;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingRepository extends R2dbcRepository<Rating, String> {

    @Query("SELECT * FROM rating WHERE rating_type_code = 'service' AND target_id = :serviceAlias AND status = 1")
    Flux<Rating> getListRatingService(String serviceAlias);

    @Query("SELECT * FROM rating WHERE id = :id")
    Mono<Rating> getById(String ratingId);

    @Query("""
            UPDATE rating
            SET target_id = :ratingId, telecom_service_id = :telecomServiceId, product_id = :productId, order_id = :orderId,
                username = :username, cust_name = :custName, rating = :rating, fix_rating = :fixRating, content = :content,
                fix_content = :fixContent, rating_date = :ratingDate, tag = :tag, fix_tag = :fixTag, has_image = :hasImage,
                has_video = :hasVideo, rating_status = :ratingStatus, state = :state, display_status = :displayStatus,
                sum_rate_status = :sumRateStatus, target = :target, update_by = :user, update_at = current_timestamp()
            WHERE id = :id
            """)
    Mono<Rating> updateRating(
            String id,
            String ratingId,
            String telecomServiceId,
            String productId,
            String orderId,
            String username,
            String custName,
            Float rating,
            Float fixRating,
            String content,
            String fixContent,
            LocalDateTime ratingDate,
            String tag,
            String fixTag,
            Integer hasImage,
            Integer hasVideo,
            Integer ratingStatus,
            String state,
            Integer displayStatus,
            Integer sumRateStatus,
            String target,
            String user);

    @Query("SELECT * FROM rating WHERE telecom_service_id = :telecomServiceId")
    Mono<Rating> getByTelecomServiceId(String telecomServiceId);

    @Query("SELECT * FROM rating WHERE product_id = :productId")
    Mono<Rating> getByProductId(String productId);

    @Query("SELECT * FROM rating WHERE order_id = :orderId")
    Mono<Rating> getByOrderId(String orderId);

    @Query("SELECT * FROM rating WHERE status = 1 ORDER BY create_at DESC")
    Flux<Rating> getAllTelecomServiceActive();

    @Query("SELECT current_timestamp")
    Mono<LocalDateTime> getSysDate();

    @Query("""
            SELECT * FROM rating
            WHERE rating_type_code = 'SERVICE' AND target_id = :serviceAlias AND status = 1
            AND display_status = 1
            AND ((:rating = 0.0) OR (rating = :rating))
            ORDER BY rating_date DESC
            LIMIT :pageSize OFFSET :index
            """)
    Flux<Rating> getListRatingServicePage(String serviceAlias, Integer pageSize, Integer index, Float rating);

    @Query(
            """
            SELECT COUNT(1)
            FROM rating
            WHERE rating_type_code = 'SERVICE' AND target_id = :serviceAlias AND status = 1
            AND display_status = 1
            AND ((:rating = 0.0) OR (rating = :rating))
            """)
    Mono<Long> getCountRatingService(String serviceAlias, Float rating);
}
