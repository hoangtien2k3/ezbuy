package com.ezbuy.orderservice.client.impl;

import static com.ezbuy.ordermodel.constants.MessageConstant.AUTH_SERVICE_ERROR;
import static com.ezbuy.ordermodel.constants.MessageConstant.GATEWAY_SERVICE_ERROR;

import com.ezbuy.ordermodel.dto.OrderFileDTO;
import com.ezbuy.ordermodel.dto.request.UploadFileBase64Request;
import com.ezbuy.orderservice.client.ApiGatewayClient;
import com.reactify.client.BaseRestClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@DependsOn("webClientFactory")
@RequiredArgsConstructor
public class ApiGatewayClientImpl implements ApiGatewayClient {

    private final BaseRestClient baseRestClient;

    @Qualifier("gatewayClient")
    private final WebClient gatewayClient;

    @Override
    public Mono<List<OrderFileDTO>> uploadFileBase64(UploadFileBase64Request uploadFileBase64Request) {
        return baseRestClient
                .post(
                        gatewayClient,
                        "v1/api-gateway/upload-file-ftp",
                        null,
                        uploadFileBase64Request,
                        DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, AUTH_SERVICE_ERROR));
                    }
                    List<OrderFileDTO> lstFile = new ArrayList<>();
                    List list = (List) dataResponseOptional.get().getData();
                    list.forEach(
                            x -> lstFile.add(ObjectMapperFactory.getInstance().convertValue(x, OrderFileDTO.class)));
                    return lstFile;
                })
                .doOnError(err -> log.error("Exception when call gateway service /upload-file-ftp: ", err))
                .onErrorResume(throwable -> Mono.error(
                        new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, GATEWAY_SERVICE_ERROR)));
    }
}
