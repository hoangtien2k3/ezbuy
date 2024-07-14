package com.ezbuy.framework.filter.properties;

import com.ezbuy.framework.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ClientLogProperties {
    private boolean enable = true;
    private List<String> obfuscateHeaders = Constants.getSensitiveHeaders();
}
