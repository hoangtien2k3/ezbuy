package com.ezbuy.ordermodel.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMemberImportListDTO {
    private List<GroupMemberImportDTO> items; // danh sach ban ghi import
    private Long totalSucceedItems; // tong so ban ghi insert thanh cong
    private Long totalFailedItems; // tong so ban ghi insert that bai
}
