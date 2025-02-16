package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.productmodel.dto.VoucherTypeDTO;
import com.ezbuy.productmodel.dto.VoucherTypeV2DTO;
import com.ezbuy.productmodel.dto.request.SearchVoucherTypeRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherTypeRepositoryTemplate {
    /**
     * Ham nay de thuc hien tim kiem
     *
     * @param request
     * @return tra ve mot list DTO cua cac loai khuyen mai thoa man yeu cau
     */
    Flux<VoucherTypeDTO> search(SearchVoucherTypeRequest request);

    /**
     * Ham nay dung de dem so luong ban ghi thoa man
     *
     * @param request
     * @return
     */
    Mono<Long> count(SearchVoucherTypeRequest request);

    /**
     * Ham lay thong tin voucher type theo voucher code
     *
     * @param code
     * @return
     */
    Flux<VoucherTypeV2DTO> findVoucherTypeByVoucherCodeUsed(String code);
}
