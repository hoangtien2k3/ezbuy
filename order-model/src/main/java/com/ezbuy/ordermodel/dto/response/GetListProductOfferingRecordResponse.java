package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.ordermodel.dto.RecordTypeInfo;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class GetListProductOfferingRecordResponse {
    @XmlElement(name = "responseCode")
    private Integer responseCode;

    @XmlElement
    private List<lst> lst;

    @Data
    public static class lst {
        private List<RecordTypeInfo> lstRecordType;
        private String productOffering;
    }
}
