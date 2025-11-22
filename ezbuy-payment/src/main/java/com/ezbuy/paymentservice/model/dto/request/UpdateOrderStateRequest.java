package com.ezbuy.paymentservice.model.dto.request;

import com.ezbuy.paymentservice.model.dto.UpdateOrderStateDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderStateRequest {
    private List<UpdateOrderStateDTO> updateOrderStateDTOList;
}
