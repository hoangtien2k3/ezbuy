package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.TelecomDTO;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    List<TelecomDTO> data;
    Long total;
}
