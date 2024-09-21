package com.ezbuy.ratingservice.repository;

import com.ezbuy.ratingmodel.model.Rating;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingRepository extends R2dbcRepository<Rating, String> {
    @Query(
            value =
                    " select * from sme_rating.rating where rating_type_code = 'service' and target_id = :serviceAlias and status = 1 ")
    Flux<Rating> getListRatingService(String serviceAlias);

    @Query("select * from sme_rating.rating where id = :id")
    Mono<Rating> getById(String ratingId);

    @Query(
            value =
                    "update sme_rating.rating set target = :ratingId, telecom_service_id = :telecomServiceId, product_id = :productId, order_id = :orderId, username = :username, cust_name = :custName, rating = :rating, fix_rating = :fixRating, content = :content, fix_content = :fixContent, rating_date = :ratingDate, tag = :tag, fix_tag = :fixTag, has_image = :hasImage, has_video = :hasVideo, rating_status = :ratingStatus, state = :state, display_status = :displayStatus, sum_rate_status = :sumRateStatus, target = :target, update_by = :user, update_at = current_timestamp() where id = :id")
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

    @Query("select * from sme_rating.rating where telecom_service_id = :telecomServiceId")
    Mono<Rating> getByTelecomServiceId(String telecomServiceId);

    @Query("select * from sme_rating.rating where product_id = :productId")
    Mono<Rating> getByProductId(String productId);

    @Query("select * from sme_rating.rating where order_id = :orderId")
    Mono<Rating> getByOrderId(String orderId);

    @Query("select * from sme_rating.rating where status = 1 ORDER BY create_at desc")
    Flux<Rating> getAllTelecomServiceActive();

    @Query("select current_timestamp")
    Mono<LocalDateTime> getSysDate();

    @Query(
            value =
                    " select * from sme_rating.rating where rating_type_code = 'SERVICE' and target_id = :serviceAlias and status = 1 and display_status = 1 and  (( :rating = 0.0 ) or ( rating = :rating )) ORDER BY rating_date desc LIMIT :pageSize OFFSET :index ")
    Flux<Rating> getListRatingServicePage(String serviceAlias, Integer pageSize, Integer index, Float rating);

    @Query(
            value =
                    " select count(1) from sme_rating.rating where rating_type_code = 'SERVICE' and target_id = :serviceAlias and status = 1 and display_status = 1 and  (( :rating = 0.0 ) or ( rating = :rating )) ")
    Mono<Long> getCountRatingService(String serviceAlias, Float rating);
}
