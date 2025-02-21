package com.ezbuy.searchservice.client.impl;

import com.ezbuy.searchservice.client.ElasticsearchClient;
import com.ezbuy.searchservice.client.properties.ElasticsearchClientProperties;
import com.ezbuy.searchservice.client.properties.IndexProperties;
import com.reactify.util.DataUtil;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class ElasticsearchClientImpl implements ElasticsearchClient {

    @Qualifier("elasticSearchClient")
    private final WebClient elasticsearchClient;

    private final ElasticsearchClientProperties elasticsearchClientProperties;
    private final IndexProperties indexProperties;

    @Override
    public Mono<String> search(String keyword, List<String> indices, Integer from, Integer size) {
        List<IndexProperties.IndexScore> indexScoreList = indexProperties.getIndexScores();
        // indicesBoost
        JSONArray indicesBoost = new JSONArray();
        for (String index : indices) {
            for (IndexProperties.IndexScore indexScore : indexScoreList) {
                if (indexScore.getName().equals(index)) {
                    indicesBoost.put(new JSONObject().put(index, Float.parseFloat(indexScore.getScore())));
                }
            }
        }

        // query
        JSONObject bool = new JSONObject();
        JSONArray should = new JSONArray();
        JSONObject multiMatch1 = new JSONObject();
        JSONObject multiMatch2 = new JSONObject();
        JSONObject multiMatchQuery1 = new JSONObject();
        JSONObject multiMatchQuery2 = new JSONObject();

        multiMatchQuery1.put("query", keyword);
        multiMatchQuery1.put("fields", new JSONArray().put("title^2").put("content"));
        multiMatchQuery1.put("operator", "or");
        multiMatchQuery1.put("fuzziness", "AUTO");
        multiMatchQuery2.put("query", keyword);
        multiMatchQuery2.put("fields", new JSONArray().put("title^2").put("content"));
        multiMatchQuery2.put("operator", "or");
        multiMatch1.put("multi_match", multiMatchQuery1);
        multiMatch2.put("multi_match", multiMatchQuery2);
        should.put(multiMatch1);
        should.put(multiMatch2);
        bool.put("should", should);

        // build highlight
        JSONObject highlight = new JSONObject();
        highlight.put("fields", new JSONObject().put("title", new JSONObject()).put("content", new JSONObject()));

        // object all
        JSONObject queryAll = new JSONObject();
        queryAll.put("indices_boost", indicesBoost);
        queryAll.put("query", new JSONObject().put("bool", bool));
        queryAll.put("highlight", highlight);
        queryAll.put("from", from);
        queryAll.put("size", size);

        StringBuilder indexString = new StringBuilder();
        for (String index : indices) {
            indexString.append(index).append(",");
        }
        indexString.deleteCharAt(indexString.length() - 1);

        String username = elasticsearchClientProperties.getUsername();
        String password = elasticsearchClientProperties.getPassword();
        String basicAuthHeader = null;
        if (!DataUtil.isNullOrEmpty(username) && !DataUtil.isNullOrEmpty(password)) {
            basicAuthHeader = "Basic "
                    + Base64.getEncoder()
                            .encodeToString((elasticsearchClientProperties.getUsername() + ":"
                                            + elasticsearchClientProperties.getPassword())
                                    .getBytes());
        }

        log.info("Query {}", queryAll);
        return elasticsearchClient
                .method(HttpMethod.GET)
                .uri("/" + indexString + "/_search")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeader)
                .body(Mono.just(queryAll.toString()), String.class)
                .retrieve()
                .bodyToMono(String.class);
    }
}
