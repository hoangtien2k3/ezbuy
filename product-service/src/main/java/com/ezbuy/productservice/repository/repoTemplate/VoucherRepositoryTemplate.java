package com.ezbuy.productservice.repository.repoTemplate;

import com.ezbuy.productmodel.dto.VoucherTypeExtDTO;
import com.ezbuy.productmodel.model.Voucher;
import com.ezbuy.productmodel.dto.request.SearchVoucherRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface VoucherRepositoryTemplate {
    //tim kiem voucher
    Flux<Voucher> queryVoucher(SearchVoucherRequest request);

    //dem so luong ban ghi voucher
    Mono<Long> countVoucher(SearchVoucherRequest request);

    /**
     * ham validate thong tin voucher co dung duoc user su dung hay khong
     *
     * @param userId
     * @param extCode
     * @param voucherCodeList
     * @return
     */
    Flux<VoucherTypeExtDTO> validateVoucher(String userId, String extCode, List<String> voucherCodeList);
}
