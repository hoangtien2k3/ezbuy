package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.sme.ordermodel.dto.recordTypeDTO;
import com.ezbuy.ordermodel.dto.recordTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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
        private List<recordTypeInfo> lstRecordType;
        private String productOffering;
    }

}
