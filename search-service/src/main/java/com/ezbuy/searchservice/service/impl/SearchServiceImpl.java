package com.ezbuy.searchservice.service.impl;

import com.reactify.util.DataUtil;
import com.reactify.util.Translator;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.ezbuy.settingmodel.constants.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ezbuy.searchmodel.dto.SearchResponseDTO;
import com.ezbuy.searchmodel.dto.request.SearchDTORequest;
import com.ezbuy.searchmodel.dto.response.SearchDTO;
import com.ezbuy.searchmodel.dto.response.SearchDTOResponse;
import com.ezbuy.searchservice.client.ElasticsearchClient;
import com.ezbuy.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchClient elasticsearchClient;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<DataResponse<Object>> search(SearchDTORequest request) {
        if (DataUtil.isNullOrEmpty(request.getKeyword())) {
            return Mono.just(new DataResponse<>(Translator.toLocale("success"), new ArrayList<>()));
        }

        List<String> stringList = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(request.getType())) {
            stringList = Constants.INDEX.ALLOW_INDEX;
        } else {
            if (Constants.INDEX.ALLOW_INDEX.contains(request.getType())) {
                stringList.add(request.getType());
            } else {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "type.invalid"));
            }
        }

        if (DataUtil.isNullOrEmpty(request.getFrom()) || request.getFrom() < 0) {
            request.setFrom(Constants.DEFAULT_VALUE.FROM);
        }
        if (DataUtil.isNullOrEmpty(request.getSize()) || request.getSize() < 0) {
            request.setSize(Constants.DEFAULT_VALUE.SIZE);
        }

        return elasticsearchClient.search(request.getKeyword(), stringList, request.getFrom(), request.getSize())
                .flatMap(rs -> {
                    SearchResponseDTO searchResponseDTO = parseResponseElasticsearch(rs);

                    // build response
                    SearchDTOResponse searchDTOResponse = new SearchDTOResponse();
                    List<SearchDTO> searchDTOList = new ArrayList<>();
                    searchResponseDTO.getHitsDTO().getHits().forEach(hitDTO -> {
                        String path = "";

                        if (Constants.INDEX.NEWS.equals(hitDTO.getIndex())) {
                            path = hitDTO.getSource().getPath();
                        } else if (Constants.INDEX.SERVICES.equals(hitDTO.getIndex())) {
                            Pattern uuidPattern = Pattern.compile("^LANDING_PAGE_(.*)");
                            Matcher matcher = uuidPattern.matcher(hitDTO.getSource().getCode());
                            if (matcher.matches()) {
                                path = String.format("landing-page?alias=%s", matcher.group(1));
                            }
                        }

                        List<String> lines =new ArrayList<>();
                        if (!DataUtil.isNullOrEmpty(hitDTO.getHighlight().getTitle())) {
                            lines.addAll(hitDTO.getHighlight().getTitle());
                        }
                        if (!DataUtil.isNullOrEmpty(hitDTO.getHighlight().getContent())) {
                            lines.addAll(hitDTO.getHighlight().getContent());
                        }

                        int maxEmTags = 0;
                        String maxEmLine = "";
                        for (String line : lines) {
                            int count = line.split("<em>").length - 1;
                            if (count > maxEmTags) {
                                maxEmTags = count;
                                maxEmLine = line;
                            }
                        }

                        searchDTOList.add(SearchDTO.builder().path(path).highlight(maxEmLine).type(hitDTO.getIndex()).score(hitDTO.getScore()).build());
                    });

                    searchDTOResponse.setResult(searchDTOList.stream().distinct().collect(Collectors.toList()));
                    DataResponse<Object> dataResponse = new DataResponse<>();
                    dataResponse.setMessage(Translator.toLocale("success"));
                    dataResponse.setData(searchDTOResponse);
                    return Mono.just(dataResponse);
                });
    }

    private SearchResponseDTO parseResponseElasticsearch(String rs) {
        try {
            return objectMapper.readValue(rs, SearchResponseDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
