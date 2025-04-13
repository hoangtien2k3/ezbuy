package com.ezbuy.authservice.service.impl;

import com.ezbuy.authmodel.dto.PaginationDTO;
import com.ezbuy.authmodel.dto.request.UserOtpRequest;
import com.ezbuy.authmodel.dto.response.SearchUserOtpResponse;
import com.ezbuy.authservice.repotemplate.impl.UserOtpRepositoryTemplateImpl;
import com.ezbuy.authservice.service.UserOtpService;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserOtpServiceImpl implements UserOtpService {

    private final UserOtpRepositoryTemplateImpl userOtpRepositoryTemplate;

    @Override
    public Mono<SearchUserOtpResponse> search(UserOtpRequest request) {
        validateSearchUserOtp(request);
        request.setPageIndex(DataUtil.safeToInt(request.getPageIndex(), 1));
        request.setPageSize(DataUtil.safeToInt(request.getPageSize(), 10));
        return Mono.zip(
                        userOtpRepositoryTemplate.search(request).collectList(),
                        userOtpRepositoryTemplate.count(request))
                .flatMap(tuple -> {
                    PaginationDTO paginationDTO = PaginationDTO.builder()
                            .pageIndex(request.getPageIndex())
                            .pageSize(request.getPageSize())
                            .totalRecords(tuple.getT2())
                            .build();
                    SearchUserOtpResponse response = SearchUserOtpResponse.builder()
                            .userOtps(tuple.getT1())
                            .pagination(paginationDTO)
                            .build();
                    return Mono.just(response);
                });
    }

    private void validateSearchUserOtp(UserOtpRequest request) {
        if (!DataUtil.isNullOrEmpty(request.getFromDate())
                && !DataUtil.isNullOrEmpty(request.getToDate())
                && request.getToDate().getDayOfYear() - request.getFromDate().getDayOfYear() > 31) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "data.search.exceed.error");
        }
    }
}
