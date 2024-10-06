package com.ezbuy.orderservice.controller;

import com.ezbuy.ordermodel.constants.UrlPaths;
import com.ezbuy.ordermodel.dto.GroupMemberImportDTO;
import com.ezbuy.ordermodel.dto.ProfileForBusinessCustDTO;
import com.ezbuy.ordermodel.dto.request.*;
import com.ezbuy.ordermodel.dto.response.GetOrderReportResponse;
import com.ezbuy.orderservice.service.OrderService;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.ezbuy.ordermodel.constants.UrlPaths.Order.CA_DOWNLOAD_GROUP_MEMBER_IMPORT_RESULT;
import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlPaths.Order.PRE_FIX)
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> searchOrder(SearchOrderRequest request) {
        return orderService.searchOrder(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping(UrlPaths.Order.DETAIL)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> findDetail(@PathVariable("orderId") String orderId) {
        return orderService.findDetail(orderId)
                .map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.PRE_ORDER)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> createPreOrder(@RequestBody CreatePreOrderRequest request) {
        return orderService.createPreOrder(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.SYNC_ORDER)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse>> syncOrder(@RequestBody SyncOrderStateRequest request) {
        return orderService.syncOrderState(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.PAID_ORDER)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> createPaidOrder(@Valid @RequestBody CreatePaidOrderRequest request) {
        return orderService.createPaidOrder(request)
                .map(ResponseEntity::ok);
    }

    /**
     * Order-008
     * Bo sung alias PYCXXX/LuongToanTrinhScontract
     * @param request
     * @return
     */
    @PostMapping(UrlPaths.Order.PAYMENT_RESULT)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse>> updateState(@RequestBody UpdateOrderStateForOrderRequest request) {
        return orderService.updateStateAndPlaceOrder(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.PRICING_PRODUCT)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> getPricingProduct(@RequestBody PricingProductRequest request) {
        return orderService.getPricingProduct(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.PRICING_PRODUCT_INTERNAL)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse>> getPricingProductInternal(@RequestBody PricingProductRequest request) {
        return orderService.getPricingProductInternal(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.ORDER_HISTORY)
    public Mono<ResponseEntity<DataResponse>> createOrderHistory(@RequestBody CreateOrderHistoryRequest request) {
        return orderService.createOrderHistory(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.VALIDATE_CONNECT_FIRST)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse>> validateConnectFirst(@RequestBody ValidateConnectServiceRequest request) {
        return orderService.validateConnectFirst(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping(UrlPaths.Order.CA_GET_GROUP_INFO)
    public Mono<ResponseEntity<DataResponse>> getCAgroupInfo(GetGroupsCAinfoRequest request) {
        return orderService.getGroupsCAInfo(request).map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.CA_ADD_GROUP_MEMBER)
    @CrossOrigin
    public Mono<ResponseEntity<DataResponse>> addCAgroupMember(@RequestBody AfterSaleGroupCARequest request) {
        return orderService.addCAgroupMember(request).map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.CA_UPDATE_GROUP_MEMBER)
    public Mono<ResponseEntity<DataResponse>> updateCAgroupMember (@RequestBody AfterSaleGroupCARequest request) {
        return orderService.updateCAgroupMember(request).map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.CA_REMOVE_GROUP_MEMBER)
    public Mono<ResponseEntity<DataResponse>> removeCAgroupMember(@RequestBody AfterSaleGroupCARequest request) {
        return orderService.removeCAgroupMember(request).map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.SERVICE_GET_DATA_PROFILE)
    public Mono<DataResponse<ProfileForBusinessCustDTO>> getProfileKHDN(@RequestBody CreateOrderPaidRequest createOrderPaidRequest){
        return orderService.getProfileKHDN(createOrderPaidRequest);
    }

    @PostMapping(UrlPaths.Order.CA_CONNECT_SELFCARE)
    public Mono<ResponseEntity<DataResponse>> connectCASelfcare(@RequestBody CreateOrderPaidRequest request) {
        return orderService.connectCASelfcare(request).map(ResponseEntity::ok);
    }

    @GetMapping(UrlPaths.Order.CA_GET_NUMBER_SIGN)
    public Mono<ResponseEntity<DataResponse>> getCAsubNumberSign(GetGroupsCAinfoRequest request) {
        return orderService.getCAsubNumberSign(request).map(ResponseEntity::ok);
    }

    @GetMapping(UrlPaths.Order.CA_GET_TOTAL_SIGN)
    public Mono<ResponseEntity<DataResponse>> getCAsubTotalSign(GetGroupsCAinfoRequest request) {
        return orderService.getCAsubTotalSign(request).map(ResponseEntity::ok);
    }

    /**
     * Order-005
     * Bo sung them alias PYCXXX/LuongToanTrinhScontract
     * @param request
     * @return
     */
    @PostMapping(UrlPaths.Order.GET_ADVICE)
    public Mono<ResponseEntity<DataResponse>> getAdvice(@RequestBody CreatePreOrderRequest request) {
        return orderService.getAdvice(request)
                .map(ResponseEntity::ok);
    }

    /**
     * Order-006
     * Bo sung them alias PYCXXX/LuongToanTrinhScontract
     * @param request
     * @return
     */
    @PostMapping(UrlPaths.Order.VALIDATE_DATA_ORDER)
    public Mono<ResponseEntity<DataResponse>> validateDataOrder(@RequestBody CreateOrderPaidRequest request) {
        return orderService.validateDataOrder(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.UPDATE_STATUS_PRE_ORDER)
    public Mono<ResponseEntity<DataResponse>> updateStatePreOrder(@RequestBody UpdateSatePreOrderRequest request) {
        return orderService.updateStatePreOrder(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.CONNECT_ORDER_SELF_CARE)
    public Mono<ResponseEntity<DataResponse>> createOrderSelfCare(@RequestBody CreateOrderPaidRequest request) {
        return orderService.createOrderSelfCare(request).map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.SERVICE_GET_DOC_DATA_POLICY)
    public Mono<DataResponse<ProfileForBusinessCustDTO>> getDocDataPolicy(@RequestBody CreateOrderPaidRequest createOrderPaidRequest){
        return orderService.getDocDataPolicy(createOrderPaidRequest);
    }

    @GetMapping(UrlPaths.Order.SEARCH_ORDER_HISTORY)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> searchOrderHistory(SearchOrderRequest request) {
        return orderService.searchOrderHistory(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping(UrlPaths.Order.SEARCH_ORDER_V2)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> searchOrderV2(@RequestBody SearchOrderV2Request request) {
        return orderService.searchOrderV2(request, request.getPreOrderCodeList())
                .map(ResponseEntity::ok);
    }

    @GetMapping(UrlPaths.Order.CA_GET_GROUP_MEMBER_INFO)
    public Mono<ResponseEntity<DataResponse>> getGroupsMemberCAInfo(GetGroupsCAinfoRequest request) {
        return orderService.getGroupsMemberCAInfo(request).map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Order.FILE_CONTRACT_TO_VIEW)
    public Mono<DataResponse<ProfileForBusinessCustDTO>> getFileContractToView(@RequestBody CreateOrderPaidRequest createOrderPaidRequest){
        return orderService.getFileContractToView(createOrderPaidRequest);
    }

    @GetMapping(UrlPaths.Order.CA_GROUP_MEMBER_TEMPLATE)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<Resource>> getImportGroupMemberTemplate() {
        return orderService.getImportGroupMemberTemplate();
    }

    @PostMapping(UrlPaths.Order.CA_GROUP_MEMBER_VALIDATE_IMPORT)
    public Mono<ResponseEntity<DataResponse>> validateImportGroupMember(
            @RequestPart("file") FilePart filePart,
            @RequestPart("totalSign") String totalSign
    ) {
        return orderService.validateImportGroupMember(filePart, totalSign).map(rs -> ResponseEntity.ok(new DataResponse(SUCCESS, rs)));
    }

    @PostMapping(value = UrlPaths.Order.CA_GROUP_MEMBER_IMPORT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> importGroupMember(
            @RequestPart("file") FilePart filePart,
            @RequestPart("groupCode") String groupCode,
            @RequestPart("organizationId") String organizationId,
            @RequestPart("groupId") String groupId,
            @RequestPart("totalSign") String totalSign
    ) {
        return orderService.importGroupMember(filePart, groupCode, organizationId, groupId, totalSign)
                .map(rs -> ResponseEntity.ok(new DataResponse(SUCCESS, rs)));
    }

    @PostMapping(value = CA_DOWNLOAD_GROUP_MEMBER_IMPORT_RESULT)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<byte[]>> downloadImportResult(@RequestBody List<GroupMemberImportDTO> items) {
        return orderService.downloadImportResult(items);
    }

    /**
     * API get order-report by day
     * @param orderReportRequest
     * @return
     */
    @GetMapping(UrlPaths.Order.ORDER_REPORT)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<DataResponse<GetOrderReportResponse>> getOrderReport(OrderReportRequest orderReportRequest){
        return orderService.getOrderReport(orderReportRequest);
    }

    @PostMapping(UrlPaths.Order.CREATE_ORDER_CTS)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> createOrderCts(@RequestBody CreateOrderPaidRequest request) {
        return orderService.createOrderCts(request).map(rs -> ResponseEntity.ok(rs));
    }
}
