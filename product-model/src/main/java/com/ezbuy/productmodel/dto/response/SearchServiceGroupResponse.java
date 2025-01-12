package com.ezbuy.productmodel.dto.response;

import com.ezbuy.productmodel.dto.ServiceGroupDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchServiceGroupResponse {
    private List<ServiceGroupDTO> lstServiceGroupDTO; // danh sach nhom dich vu
    private PaginationDTO pagination; // thong tin phan trang
}
