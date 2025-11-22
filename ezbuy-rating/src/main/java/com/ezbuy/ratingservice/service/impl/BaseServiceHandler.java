package com.ezbuy.ratingservice.service.impl;

import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.exception.BusinessException;

public class BaseServiceHandler {
    public int validatePageSize(Integer pageSize, int defaultPageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        } else if (pageSize <= 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "params.pageSize.invalid");
        }
        return pageSize;
    }

    public int validatePageIndex(Integer pageIndex) {
        if (pageIndex == null) {
            pageIndex = 1;
        } else if (pageIndex < 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "params.pageIndex.invalid");
        }
        return pageIndex;
    }
}
