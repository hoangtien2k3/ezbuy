package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.ordermodel.dto.GroupCAInfo;
import lombok.Data;

import java.util.List;

@Data
public class GetGroupCAInfoResponse {
    private List<GroupCAInfo> groupCAInfoList;
    private Long totalRecord;//tong so ban ghi
}
