package com.ezbuy.productservice.controller;

import static com.ezbuy.productmodel.constants.UrlPaths.*;

import com.ezbuy.productmodel.dto.FilterProductTemplateDTO;
import com.ezbuy.productmodel.request.GetListProductOfferingComboForHubSmeRequest;
import com.ezbuy.productmodel.request.GetProductTemplateDetailRequest;
import com.ezbuy.productservice.service.ProductOfferTemplateService;
import com.ezbuy.productservice.service.ProductSpecService;
import com.ezbuy.productservice.service.PublicService;
import com.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * author duclv
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(DEFAULT_V1_PREFIX)
public class PublicController {

    // inject
    private final ProductSpecService productSpecService;
    private final ProductOfferTemplateService productOfferTemplateService;
    private final PublicService publicService;
    // inject

    /**
     * get filter for filter product template
     *
     * @param telecomServiceAlias
     * @return
     */
    @GetMapping(value = FILTER_TEMPLATE)
    public Mono<DataResponse> getFilterTemplate(@RequestParam(defaultValue = "") String telecomServiceAlias) {
        return productSpecService.getFilterTemplate(telecomServiceAlias);
    }

    /**
     * get list product by filtering
     *
     * @param filterProductTemplateDTO
     * @return
     */
    @PostMapping(value = FILTER_PRODUCT_TEMPLATE)
    public Mono<DataResponse> getFilterProductTemplate(@RequestBody FilterProductTemplateDTO filterProductTemplateDTO) {
        return productOfferTemplateService.getProductTemplate(filterProductTemplateDTO);
    }

    /**
     * get list product for mega menu
     *
     * @param
     * @return
     */
    @GetMapping(value = GET_PRODUCT_FOR_MEGA_MENU)
    public Mono<DataResponse> getProductsForMegaMenu() {
        return productOfferTemplateService.getProductsForMegaMenu();
    }

    /**
     * lay danh sach goi cuoc combo
     *
     * @param
     * @return
     */
    @GetMapping(value = GET_LIST_PRODUCT_OFFERING_COMBO)
    public Mono<DataResponse> getListProductOfferingComboForHubSme() {
        return publicService.getListProductOfferingComboForHubSme(new GetListProductOfferingComboForHubSmeRequest());
    }

    @PostMapping(value = GET_LIST_PRODUCT_OFFERING_COMBO_V2)
    public Mono<DataResponse> getListProductOfferingComboForHubSmeV2(
            @RequestBody GetListProductOfferingComboForHubSmeRequest request) {
        return publicService.getListProductOfferingComboForHubSme(request);
    }

    /**
     * lay danh sach chi tiet goi combo
     *
     * @param productOfferingId
     *            id goi cuoc
     * @return
     */
    @GetMapping(value = GET_LIST_TEMPLATE_COMBO)
    public Mono<DataResponse> getListTemplateComboForHubSme(
            @RequestParam(value = "productOfferingId", required = false) String productOfferingId) {
        return publicService.getListTemplateComboForHubSme(productOfferingId);
    }

    /**
     * lay thong tin bo sung cho man hinh chon goi combo
     *
     * @param
     * @return
     */
    @PostMapping(value = GET_PRODUCT_TEMPLATE_DETAIL)
    public Mono<DataResponse> getProductTemplateDetail(
            @RequestBody GetProductTemplateDetailRequest getProductTemplateDetailRequest) {
        return publicService.getProductTemplateDetail(getProductTemplateDetailRequest);
    }
}
