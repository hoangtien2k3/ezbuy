package com.ezbuy.productservice.service.impl;

import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;

import com.ezbuy.productmodel.request.GetListProductOfferingComboForHubSmeRequest;
import com.ezbuy.productmodel.request.GetProductTemplateDetailRequest;
import com.ezbuy.productservice.client.ProductClient;
import com.ezbuy.productservice.service.PublicService;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.annotations.LocalCache;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicServiceImpl implements PublicService {
    private final ProductClient productClient;

    @Override
    @LocalCache(durationInMinute = 30)
    public Mono<DataResponse> getListProductOfferingComboForHubSme(
            GetListProductOfferingComboForHubSmeRequest request) {
        return productClient
                .getListProductOfferingComboForHubSme(request)
                .flatMap(productOfferingComboForHubSme ->
                        Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), productOfferingComboForHubSme)));
    }

    @Override
    @LocalCache(durationInMinute = 30)
    public Mono<DataResponse> getListTemplateComboForHubSme(String productOfferingId) {
        return productClient
                .getListTemplateComboForHubSme(productOfferingId)
                .flatMap(productTemplateComboForHubSme ->
                        Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), productTemplateComboForHubSme)));
    }

    @Override
    @LocalCache(durationInMinute = 30)
    public Mono<DataResponse> getProductTemplateDetail(
            GetProductTemplateDetailRequest getProductTemplateDetailRequest) {
        return productClient
                .getProductTemplateDetail(getProductTemplateDetailRequest)
                .map(response -> {
                    if (response.isEmpty()) {
                        return new DataResponse<>(Translator.toLocaleVi(SUCCESS), null);
                    }
                    return new DataResponse<>(Translator.toLocaleVi(SUCCESS), response.get());
                });
    }
}
