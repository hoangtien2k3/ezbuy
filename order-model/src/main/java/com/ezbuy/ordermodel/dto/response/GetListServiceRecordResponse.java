package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.ordermodel.dto.ServiceInfo;
import com.ezbuy.ordermodel.dto.recordTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class GetListServiceRecordResponse {
    @XmlElement(name = "responseCode")
    private Integer responseCode;
    @XmlElement
    private List<lst> lst;

    @Data
    public static class lst {
        private List<recordTypeInfo> lstRecordType;
        private ServiceInfo serviceInfo;
    }
}
