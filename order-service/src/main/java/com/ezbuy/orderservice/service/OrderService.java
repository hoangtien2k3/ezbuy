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

    /**
     * Ham lay thong tin nhom
     *
     * @param request
     * @return
     */
    Mono<DataResponse<GetGroupCAInfoResponse>> getGroupsCAInfo(GetGroupsCAinfoRequest request);

    /**
     * Cap nhat thue bao nhom
     *
     * @param request
     * @return
     */
    Mono<DataResponse> updateCAgroupMember(AfterSaleGroupCARequest request);

    /**
     * Xoa thue bao khoi nhom
     *
     * @param request
     * @return
     */
    Mono<DataResponse> removeCAgroupMember(AfterSaleGroupCARequest request);

    /**
     * Ham tao don hang khong mat phi
     *
     * @param orderType
     * @param orderData
     * @param userId
     *            lay tu token
     * @param username
     *            lay tu token
     * @return
     */
    Mono<DataResponse> createNotPaidOrder(
            String orderType, PlaceOrderData orderData, String userId, String username, String individualId);

    Mono<DataResponse> connectCASelfcare(CreateOrderPaidRequest request);

    Mono<DataResponse<ProfileForBusinessCustDTO>> getProfileKHDN(CreateOrderPaidRequest createOrderPaidRequest);

    Mono<DataResponse> getAdvice(CreatePreOrderRequest request);

    Mono<DataResponse> validateDataOrder(CreateOrderPaidRequest data);

    /**
     * Ham cap nhat trang thai don hang PreOrder
     *
     * @param request
     * @return
     */
    Mono<DataResponse<Object>> updateStatePreOrder(UpdateSatePreOrderRequest request);

    /**
     * Ham tao don hang di tu man selfCare SME_HUB
     *
     * @param request
     * @return
     */
    Mono<DataResponse> createOrderSelfCare(CreateOrderPaidRequest request);

    Mono<DataResponse<ProfileForBusinessCustDTO>> getDocDataPolicy(CreateOrderPaidRequest createOrderPaidRequest);

    /**
     * Ham tim kiem lich su don hang
     *
     * @param request
     * @return
     */
    Mono<DataResponse> searchOrderHistory(SearchOrderRequest request);

    Mono<DataResponse> searchOrderV2(SearchOrderRequest request, List<String> preOrderCodeList);

    /**
     * Lay thong tin thue bao thanh vien nhom CA
     *
     * @param request
     * @return
     */
    Mono<DataResponse<GetGroupCAInfoResponse>> getGroupsMemberCAInfo(GetGroupsCAinfoRequest request);

    /**
     * api view file hsdt v2
     *
     * @param createOrderPaidRequest
     * @return
     */
    Mono<DataResponse<ProfileForBusinessCustDTO>> getFileContractToView(CreateOrderPaidRequest createOrderPaidRequest);

    /**
     * Ham tai file import mau cho import thue bao nhom CA
     *
     * @return
     */
    Mono<ResponseEntity<Resource>> getImportGroupMemberTemplate();

    /**
     * Ham validate thong tin import
     *
     * @param filePart
     * @return
     */
    Mono<GroupMemberImportListDTO> validateImportGroupMember(FilePart filePart, String totalSign);

    /**
     * Ham tra ve ket qua import file
     *
     * @param items
     * @return
     */
    Mono<ResponseEntity<byte[]>> downloadImportResult(List<GroupMemberImportDTO> items);

    Mono<DataResponse<GetOrderReportResponse>> getOrderReport(OrderReportRequest request);

    /**
     * Ham tao don xac thuc doanh nghiep
     *
     * @param request
     * @return
     */
    Mono<DataResponse> createOrderCts(CreateOrderPaidRequest request);

    Mono<DataResponse> getOrderTransactionFromTo(GetOrderTransactionToRequest request);
}
