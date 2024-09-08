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
package com.ezbuy.ratingmodel.constants;

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
