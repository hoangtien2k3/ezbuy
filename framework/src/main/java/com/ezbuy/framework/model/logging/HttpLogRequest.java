package com.ezbuy.framework.model.logging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hoangtien2k3
 * <p>
 * cau hinh chi tiet log cho http request.
 * <p>
 * Neu enable = true => cho phep log http request;
 * <p>
 * header = true => cho phep log http request header
 * <p>
 * param = true => cho phep log http request param
 * <p>
 * body = true => cho phep log http request body
 * <p>
 * Khong sua cau hinh file nay; muon customize log thi cau hinh trong file properties.yaml
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogRequest {
    private boolean enable = true;
}
