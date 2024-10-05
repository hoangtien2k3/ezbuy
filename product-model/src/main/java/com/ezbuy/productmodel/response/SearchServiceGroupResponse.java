package com.ezbuy.productmodel.response;

import com.ezbuy.productmodel.dto.ServiceGroupDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchServiceGroupResponse {
    private List<ServiceGroupDTO> lstServiceGroupDTO; //danh sach nhom dich vu
    private PaginationDTO pagination; //thong tin phan trang
}
