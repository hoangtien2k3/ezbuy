package com.ezbuy.orderservice.client;

import com.ezbuy.ordermodel.dto.OrderFileDTO;
import com.ezbuy.ordermodel.dto.request.UploadFileBase64Request;
import java.util.List;
import reactor.core.publisher.Mono;

public interface ApiGatewayClient {

    Mono<List<OrderFileDTO>> uploadFileBase64(UploadFileBase64Request uploadFileBase64Request);
}
