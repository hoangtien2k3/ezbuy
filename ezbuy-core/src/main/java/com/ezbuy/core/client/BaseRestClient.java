/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến.
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
package com.ezbuy.core.client;

import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <p>
 * BaseRestClient interface provides basic REST client operations.
 * </p>
 *
 * @author hoangtien2k3
 */
public interface BaseRestClient {

    /**
     * <p>
     * Sends a GET request to the specified URL with optional headers and payload.
     * </p>
     *
     * @param webClient   the
     *                    {@link org.springframework.web.reactive.function.client.WebClient}
     *                    instance to use for the request
     * @param url         the target URL for the GET request
     * @param headerList  a {@link org.springframework.util.MultiValueMap} of headers to
     *                    include in the request
     * @param payload     a {@link org.springframework.util.MultiValueMap} of query
     *                    parameters to send with the request
     * @param resultClass the {@link java.lang.Class} of the expected response body type
     * @return a {@link reactor.core.publisher.Mono} containing the response body
     * wrapped in an {@link java.util.Optional}, or an empty Optional if not
     * found
     */
    <T> Mono<Optional<T>> get(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> payload,
            Class<T> resultClass
    );

    /**
     * <p>
     * Sends a GET request to the specified URL with optional headers and payload.
     * </p>
     *
     * @param webClient the
     *                  {@link org.springframework.web.reactive.function.client.WebClient}
     *                  instance to use for the request
     * @param url       the target URL for the GET request
     * @param headers   a {@link org.springframework.util.MultiValueMap} of headers to
     *                  include in the request
     * @param payload   a {@link org.springframework.util.MultiValueMap} of query
     *                  parameters to send with the request
     * @param typeRef   the ParameterizedTypeReference typeRef
     * @return a {@link reactor.core.publisher.Mono} containing the response body
     * wrapped in an {@link java.util.Optional}, or an empty Optional if not
     * found
     */
    <T> Mono<Optional<T>> get(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headers,
            MultiValueMap<String, String> payload,
            ParameterizedTypeReference<T> typeRef
    );

    /**
     * <p>
     * post.
     * </p>
     *
     * @param webClient   a
     *                    {@link org.springframework.web.reactive.function.client.WebClient}
     *                    object
     * @param url         a {@link java.lang.String} object
     * @param headerList  a {@link org.springframework.util.MultiValueMap} object
     * @param payload     a {@link java.lang.Object} object
     * @param resultClass a {@link java.lang.Class} object
     * @return a {@link reactor.core.publisher.Mono} object
     */
    <T> Mono<Optional<T>> post(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<T> resultClass
    );

    /**
     * <p>
     * post.
     * </p>
     *
     * @param webClient  a
     *                   {@link org.springframework.web.reactive.function.client.WebClient}
     *                   object
     * @param url        a {@link java.lang.String} object
     * @param headerList a {@link org.springframework.util.MultiValueMap} object
     * @param payload    a {@link java.lang.Object} object
     * @param typeRef    a ParameterizedTypeReference typeRef
     * @return a {@link reactor.core.publisher.Mono} object
     */
    <T> Mono<Optional<T>> post(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            ParameterizedTypeReference<T> typeRef
    );

    /**
     * <p>
     * post.
     * </p>
     *
     * @param webClient  a
     *                   {@link org.springframework.web.reactive.function.client.WebClient}
     *                   object
     * @param url        a {@link java.lang.String} object
     * @param headerList a {@link org.springframework.util.MultiValueMap} object
     * @param formData   a {@link org.springframework.util.MultiValueMap} object
     * @param typeRef    a ParameterizedTypeReference<T> typeRef
     * @return a {@link reactor.core.publisher.Mono} object
     */
    <T> Mono<Optional<T>> post(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> formData,
            ParameterizedTypeReference<T> typeRef
    );

    /**
     * <p>
     * post.
     * </p>
     *
     * @param webClient   a
     *                    {@link org.springframework.web.reactive.function.client.WebClient}
     *                    object
     * @param url         a {@link java.lang.String} object
     * @param headerList  a {@link org.springframework.util.MultiValueMap} object
     * @param formData    a {@link org.springframework.util.MultiValueMap} object
     * @param resultClass a Class<T> resultClass
     * @return a {@link reactor.core.publisher.Mono} object
     */
    <T> Mono<Optional<T>> post(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> formData,
            Class<T> resultClass
    );

    /**
     * <p>
     * put.
     * </p>
     *
     * @param webClient   a
     *                    {@link org.springframework.web.reactive.function.client.WebClient}
     *                    object
     * @param url         a {@link java.lang.String} object
     * @param headerList  a {@link org.springframework.util.MultiValueMap} object
     * @param payload     a Object payload
     * @param resultClass a Class<T> resultClass
     * @return a {@link reactor.core.publisher.Mono} object
     */
    <T> Mono<Optional<T>> put(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            Class<T> resultClass
    );

    /**
     * <p>
     * put.
     * </p>
     *
     * @param webClient  a
     *                   {@link org.springframework.web.reactive.function.client.WebClient}
     *                   object
     * @param url        a {@link java.lang.String} object
     * @param headerList a {@link org.springframework.util.MultiValueMap} object
     * @param payload    a Object payload
     * @param typeRef    a ParameterizedTypeReference typeRef
     * @return a {@link reactor.core.publisher.Mono} object
     */
    <T> Mono<Optional<T>> put(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            Object payload,
            ParameterizedTypeReference<T> typeRef
    );

    /**
     * <p>
     * delete.
     * </p>
     *
     * @param webClient   a
     *                    {@link org.springframework.web.reactive.function.client.WebClient}
     *                    object
     * @param url         a {@link java.lang.String} object
     * @param headerList  a {@link org.springframework.util.MultiValueMap} object
     * @param payload     a {@link org.springframework.util.MultiValueMap} object
     * @param resultClass a {@link java.lang.Class} object
     * @return a {@link reactor.core.publisher.Mono} object
     */
    <T> Mono<Optional<T>> delete(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> payload,
            Class<T> resultClass
    );

    /**
     * <p>
     * delete.
     * </p>
     *
     * @param webClient   a
     *                    {@link org.springframework.web.reactive.function.client.WebClient}
     *                    object
     * @param url         a {@link java.lang.String} object
     * @param headerList  a {@link org.springframework.util.MultiValueMap} object
     * @param payload     a {@link org.springframework.util.MultiValueMap} object
     * @param typeRef a ParameterizedTypeReference<T> typeRef
     * @return a {@link reactor.core.publisher.Mono} object
     */
    <T> Mono<Optional<T>> delete(
            WebClient webClient,
            String url,
            MultiValueMap<String, String> headerList,
            MultiValueMap<String, String> payload,
            ParameterizedTypeReference<T> typeRef
    );

    /**
     * <p>
     * proxyClient.
     * </p>
     *
     * @param proxyHost   a {@link java.lang.String} object
     * @param proxyPort   a {@link java.lang.Integer} object
     * @param proxyEnable a {@link java.lang.Boolean} object
     * @return a {@link org.springframework.web.reactive.function.client.WebClient}
     * object
     */
    WebClient proxyClient(
            String proxyHost,
            Integer proxyPort,
            Boolean proxyEnable
    );

    /**
     * <p>
     * proxyHttpClient.
     * </p>
     *
     * @param proxyHost a {@link java.lang.String} object
     * @param proxyPort a {@link java.lang.Integer} object
     * @return a {@link org.springframework.web.reactive.function.client.WebClient}
     * object
     */
    WebClient proxyHttpClient(
            String proxyHost,
            Integer proxyPort
    );

    /**
     * Creates a new {@link ParameterizedTypeReference} instance for the specified generic type {@code <T>}.
     * <p>
     * This utility method provides a concise way to obtain a type reference with preserved
     * generic type information at runtime. It is particularly useful when working with
     * {@link org.springframework.web.reactive.function.client.WebClient} or
     * {@link org.springframework.web.client.RestTemplate}, where Java’s type erasure would otherwise
     * remove the generic type information.
     * </p>
     *
     * <p><b>Example usage:</b></p>
     * <pre>{@code
     * Mono<DataResponse<String>> result = webClient.get()
     *      .uri("/v1/example")
     *      .retrieve()
     *      .bodyToMono(typeOf());
     * }</pre>
     *
     * @param <T> the generic type to capture
     * @return a {@link ParameterizedTypeReference} instance representing the runtime type of {@code <T>}
     */
    static <T> ParameterizedTypeReference<T> typeOf() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
