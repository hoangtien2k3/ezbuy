package com.ezbuy.ordermodel.dto.sale;

import java.util.Date;
import lombok.Data;

@Data
public class CustTypeDTO extends BaseDTO {
    protected Date createDatetime;
    protected String createUser;
    protected String custType;
    protected String description;
    protected String groupType;
    protected String name;
    protected String plan;
    protected String representCust;
    protected String status;
    protected Long tax;
    protected Date updateDatetime;
    protected String updateUser;
}
