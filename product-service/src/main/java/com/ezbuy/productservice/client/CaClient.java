package com.ezbuy.productservice.client;

import com.ezbuy.sme.productmodel.request.ValidateSubInsRequest;
import com.ezbuy.sme.productmodel.request.getListAreaInsRequest;
import com.ezbuy.sme.productmodel.response.ListAreaInsResponse;
import com.ezbuy.sme.productmodel.response.ValidateSubInsResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface CaClient {
    /**
     * Ham validate thue bao vBHXH
     * @param request
     * @return
     */
    Mono<Optional<ValidateSubInsResponse>> validateSubIns(ValidateSubInsRequest request);

    /**
     * Ham lay danh sach tinh/co quan quan ly
     * @param request
     * @return
     */
    Mono<Optional<ListAreaInsResponse>> getListAreaIns(getListAreaInsRequest request);
}
