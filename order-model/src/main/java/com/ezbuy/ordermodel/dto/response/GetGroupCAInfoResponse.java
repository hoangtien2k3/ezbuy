package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.ordermodel.dto.GroupCAInfo;
import java.util.List;
import lombok.Data;

@Data
public class GetGroupCAInfoResponse {
    private List<GroupCAInfo> groupCAInfoList;
    private Long totalRecord; // tong so ban ghi
}
