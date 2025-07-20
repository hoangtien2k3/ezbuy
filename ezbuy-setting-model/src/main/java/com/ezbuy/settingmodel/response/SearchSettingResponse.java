package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.dto.SettingDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchSettingResponse {
    private List<SettingDTO> settingPage; // Danh sách setting
    private PaginationDTO pagination; // Phan trang
}
