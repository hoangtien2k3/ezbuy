package com.ezbuy.ordermodel.dto.sale;

import java.util.Date;
import lombok.Data;

@Data
public class GroupsExtDTO extends BaseDTO {
    protected Date createDatetime;
    protected String createUser;
    protected String extKey;
    protected String extValue;
    protected Long groupsId;
    protected Long id;
    protected String incentivesMethodId;
    protected String status;
    protected Date updateDatetime;
    protected String updateUser;
}
