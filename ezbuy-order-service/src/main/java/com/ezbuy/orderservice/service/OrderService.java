package com.ezbuy.orderservice.service;

import com.ezbuy.ordermodel.dto.GroupMemberImportDTO;
import com.ezbuy.ordermodel.dto.GroupMemberImportListDTO;
import com.ezbuy.ordermodel.dto.PlaceOrderData;
import com.ezbuy.ordermodel.dto.ProfileForBusinessCustDTO;
import com.ezbuy.ordermodel.dto.request.*;
import com.ezbuy.ordermodel.dto.response.GetGroupCAInfoResponse;
import com.ezbuy.ordermodel.dto.response.GetOrderReportResponse;
import com.reactify.model.response.DataResponse;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface OrderService {

    Mono<DataResponse> searchOrder(SearchOrderRequest request);

    Mono<DataResponse> findDetail(String orderId);

    Mono<DataResponse> syncOrderState(SyncOrderStateRequest request);

    Mono<DataResponse> createPaidOrder(CreatePaidOrderRequest request);

    Mono<DataResponse<Object>> updateStateAndPlaceOrder(UpdateOrderStateForOrderRequest request);

    Mono<DataResponse> getPricingProduct(PricingProductRequest request);

    Mono<DataResponse> getPricingProductInternal(PricingProductRequest request);

    Mono<DataResponse> createOrderHistory(CreateOrderHistoryRequest request);

    Mono<DataResponse<GetGroupCAInfoResponse>> getGroupsCAInfo(GetGroupsCAinfoRequest request);

    Mono<DataResponse> updateCAgroupMember(AfterSaleGroupCARequest request);

    Mono<DataResponse> removeCAgroupMember(AfterSaleGroupCARequest request);

    Mono<DataResponse> createNotPaidOrder(
            String orderType, PlaceOrderData orderData, String userId, String username, String individualId);

    Mono<DataResponse> connectCASelfcare(CreateOrderPaidRequest request);

    Mono<DataResponse<ProfileForBusinessCustDTO>> getProfileKHDN(CreateOrderPaidRequest createOrderPaidRequest);

    Mono<DataResponse> getAdvice(CreatePreOrderRequest request);

    Mono<DataResponse> validateDataOrder(CreateOrderPaidRequest data);

    Mono<DataResponse<Object>> updateStatePreOrder(UpdateSatePreOrderRequest request);

    Mono<DataResponse> createOrderSelfCare(CreateOrderPaidRequest request);

    Mono<DataResponse<ProfileForBusinessCustDTO>> getDocDataPolicy(CreateOrderPaidRequest createOrderPaidRequest);

    Mono<DataResponse> searchOrderHistory(SearchOrderRequest request);

    Mono<DataResponse> searchOrderV2(SearchOrderRequest request, List<String> preOrderCodeList);

    Mono<DataResponse<GetGroupCAInfoResponse>> getGroupsMemberCAInfo(GetGroupsCAinfoRequest request);

    Mono<DataResponse<ProfileForBusinessCustDTO>> getFileContractToView(CreateOrderPaidRequest createOrderPaidRequest);

    Mono<ResponseEntity<Resource>> getImportGroupMemberTemplate();

    Mono<GroupMemberImportListDTO> validateImportGroupMember(FilePart filePart, String totalSign);

    Mono<ResponseEntity<byte[]>> downloadImportResult(List<GroupMemberImportDTO> items);

    Mono<DataResponse<GetOrderReportResponse>> getOrderReport(OrderReportRequest request);

    Mono<DataResponse> createOrderCts(CreateOrderPaidRequest request);

    Mono<DataResponse> getOrderTransactionFromTo(GetOrderTransactionToRequest request);
}
