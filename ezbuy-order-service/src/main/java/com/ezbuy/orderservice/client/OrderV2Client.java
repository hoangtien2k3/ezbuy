package com.ezbuy.orderservice.client;

import com.ezbuy.ordermodel.dto.ProfileForBusinessCustDTO;
import com.ezbuy.ordermodel.dto.request.SearchOrderRequest;
import com.ezbuy.ordermodel.dto.response.GetOrderHistoryResponse;
import com.ezbuy.ordermodel.dto.ws.CreateOrderResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface OrderV2Client {
    Mono<ProfileForBusinessCustDTO> getProfileKHDN(
            String data, Long telecomServiceId, HashMap<String, Object> metaData);

    Mono<Optional<CreateOrderResponse>> createOrder(String orderType, String data);

    /**
     * Ham lay van ban chinh sach xu ly va bao ve du lieu
     *
     * @param data
     * @return
     */
    Mono<ProfileForBusinessCustDTO> getProfileXNDLKH(String data);

    /**
     * Ham goi sang Order lay danh sach lich su don hang
     *
     * @param request
     * @param systemTypeList
     * @param orderTypeList
     * @return
     */
    Mono<Optional<List<GetOrderHistoryResponse>>> getOrderHistoryHub(
            SearchOrderRequest request, List<String> systemTypeList, List<String> orderTypeList);

    /**
     * Ham goi sang lay file view hop dong
     *
     * @param orderType
     *            loai don hang
     * @param data
     *            data duoi dang json
     * @return
     */
    Mono<ProfileForBusinessCustDTO> getFileContractToView(String orderType, String data);
}
