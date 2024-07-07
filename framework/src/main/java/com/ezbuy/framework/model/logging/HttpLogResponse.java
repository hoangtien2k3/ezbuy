package com.ezbuy.framework.model.logging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogResponse {
    private boolean enable = true;
}
