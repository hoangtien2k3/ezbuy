package com.ezbuy.productservice.client;

import com.ezbuy.productmodel.dto.request.ValidateSubInsRequest;
import com.ezbuy.productmodel.dto.request.getListAreaInsRequest;
import com.ezbuy.productmodel.dto.response.ListAreaInsResponse;
import com.ezbuy.productmodel.dto.response.ValidateSubInsResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface CaClient {
    /**
     * Ham validate thue bao vBHXH
     *
     * @param request
     * @return
     */
    Mono<Optional<ValidateSubInsResponse>> validateSubIns(ValidateSubInsRequest request);

    /**
     * Ham lay danh sach tinh/co quan quan ly
     *
     * @param request
     * @return
     */
    Mono<Optional<ListAreaInsResponse>> getListAreaIns(getListAreaInsRequest request);
}
