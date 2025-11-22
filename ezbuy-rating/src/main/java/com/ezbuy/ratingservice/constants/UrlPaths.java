package com.ezbuy.ratingservice.constants;

public class UrlPaths {
    public interface RatingService {
        String PREFIX = "/v1/rating";
    }

    public interface Rating {
        String PREFIX = "/v1/rating";
        String GET_ALL_RATTING_ACTIVE = "/all-active";
        String GET_RATTING_SERVICE = "/rating-service";
        String UPDATE = "/{id}";
        String ALL = "/all";
        String SEARCH = "/search";
        String GET_RATTING_SERVICE_PAGING = "/rating-service-paging";
    }

    public interface RatingType {
        String PREFIX = "/v1/rating-type";
        String GET_ALL_ACTIVE = "/all-active";
    }
}
