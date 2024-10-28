package com.ezbuy.ordermodel.dto.request;

import com.ezbuy.ordermodel.dto.OrderFileDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFileBase64Request {
    private List<OrderFileDTO> lstFile;
    private String folderName; // ten folder luu tru file
}
