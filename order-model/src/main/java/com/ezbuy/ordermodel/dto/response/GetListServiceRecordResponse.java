package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.ordermodel.dto.RecordTypeInfo;
import com.ezbuy.ordermodel.dto.ServiceInfo;
import java.util.List;
import javax.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private List<RecordTypeInfo> lstRecordType;
        private ServiceInfo serviceInfo;
    }
}
