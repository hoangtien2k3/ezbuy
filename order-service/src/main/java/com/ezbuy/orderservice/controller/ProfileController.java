package com.ezbuy.orderservice.controller;

import com.ezbuy.ordermodel.constants.UrlPaths;
import com.ezbuy.ordermodel.dto.request.GetListProductOfferingRecordRequest;
import com.ezbuy.ordermodel.dto.request.GetListServiceRecordRequest;
import com.ezbuy.orderservice.service.ProfileService;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.constants.MessageConstant;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.ezbuy.ordermodel.constants.UrlPaths.Order.GET_LIST_PRODUCT_OFFERING_RECORD_PROFILE;
import static com.ezbuy.ordermodel.constants.UrlPaths.Order.GET_LIST_SERVICE_RECORD_PROFILE;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlPaths.Order.PRE_FIX)
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping(value=GET_LIST_SERVICE_RECORD_PROFILE)
    public Mono<DataResponse> getListServiceRecord(@RequestBody GetListServiceRecordRequest request) {
            return profileService.getListServiceRecord(request)
                .map(result -> new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), result));
    }

    @PostMapping(value=GET_LIST_PRODUCT_OFFERING_RECORD_PROFILE)
    public Mono<DataResponse> getListProductOfferingRecord(@RequestBody GetListProductOfferingRecordRequest request) {
        return profileService.getListProductOfferingRecord(request)
                .map(result -> new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), result));
    }
}
