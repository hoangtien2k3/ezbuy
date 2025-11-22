package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.PaginationDTO;
import com.ezbuy.settingservice.model.dto.SettingDTO;
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
