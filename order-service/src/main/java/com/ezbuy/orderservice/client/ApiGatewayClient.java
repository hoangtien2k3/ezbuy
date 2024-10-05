package com.ezbuy.orderservice.client;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.ordermodel.dto.OrderFileDTO;
import com.ezbuy.sme.ordermodel.dto.request.UploadFileBase64Request;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ApiGatewayClient {

    Mono<List<OrderFileDTO>> uploadFileBase64(UploadFileBase64Request uploadFileBase64Request);

}
