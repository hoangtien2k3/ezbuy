package com.ezbuy.productmodel.dto.request;

import com.ezbuy.productmodel.constants.enumeration.SortOrder;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiUtils {
    private SortOrder sortOrder;
    private String sortField;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer pageCount;
    private List<FilterRequest> listFilter;
}
