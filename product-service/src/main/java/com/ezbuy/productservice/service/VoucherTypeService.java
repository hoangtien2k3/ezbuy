package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.VoucherTypeV2DTO;
import com.ezbuy.productmodel.dto.request.SearchVoucherTypeRequest;
import com.ezbuy.productmodel.dto.request.VoucherTypeRequest;
import com.ezbuy.productmodel.dto.response.SearchVoucherTypeResponse;
import com.ezbuy.productmodel.model.VoucherType;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface VoucherTypeService {
    Mono<DataResponse<List<VoucherType>>> getAllVoucherTypeActive();
    /**
     * Author: HaPT
     * Ham nay dung de tra ve tat ca ban ghi loai khuyen mai
     * @return list setting  database
     */
    Mono<List<VoucherType>> getAll();

    /**
     * Ham nay dung de tao moi ban ghi loai khuyen mai
     * @param request
     * @return mot ban ghi moi duoc tao
     */
    Mono<DataResponse<VoucherType>> create(VoucherTypeRequest request);

    /**
     * Ham nay dung de update mot ban ghi loai khuyen mai
     * @param id
     * @param request
     * @return mot ban ghi vua duoc update
     */
    Mono<DataResponse<VoucherType>> update(String id, VoucherTypeRequest request);

    /**
     * Ham nay dung de search cac ban ghi loai khuyen mai dua vao request
     * @param request
     * @return mot list cac ban ghi va thong tin phan trang( tong so ban ghi, so trang, trang hien tai)
     */
    Mono<SearchVoucherTypeResponse> search(SearchVoucherTypeRequest request);

    /**
     * Ham nay de xoa mot ban ghi loai khuyen mai( mass delete)
     * dat truong status ve 0
     * @param id
     * @return tra ve gia tri ban ghi vua duoc cap nhat
     */
    Mono<DataResponse<VoucherType>> delete(String id);

    /**
     * Ham tim kiem voucher type theo voucher code
     * @param voucherCode
     * @return
     */
    Mono<DataResponse<List<VoucherTypeV2DTO>>> findVoucherTypeByVoucherCode(String voucherCode);
}
