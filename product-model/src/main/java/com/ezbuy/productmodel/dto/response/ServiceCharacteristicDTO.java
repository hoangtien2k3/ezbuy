package com.ezbuy.productmodel.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "data")
public class ServiceCharacteristicDTO {

    private Boolean success;
    private Boolean checkImport;
    private Boolean expandRow;
    private Boolean expandRowDetail;
    private Boolean expandRowProduct;
    private Boolean checkPrice;

    private String code;

    private String name;

    private ProductSpecCharValueDTO productSpecCharValueDTO;

    private List<ProductSpecCharValueDTO> productSpecCharValueDTOList;

    @JsonIgnore
    private String telecomServiceId;

    @JsonIgnore
    private String telecomServiceAlias;
}
