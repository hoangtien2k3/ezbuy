package com.ezbuy.ordermodel.dto.sale;

import java.util.Date;
import lombok.Data;

@Data
public class CustContactDTO extends BaseDTO {
    protected String billAddress;
    protected String contactLevel;
    protected String contactName;
    protected String contactTittle;
    protected String contactType;
    protected String contents;
    protected Date createDatetime;
    protected String createUser;
    protected Long custContactId;
    protected Long custId;
    protected CustomerDTO customerDTO;
    protected String email;
    protected String errorEmailMsg;
    protected String errorTelephoneNumberMsg;
    protected String status;
    protected String telephone;
    protected Date updateDatetime;
    protected String updateUser;
}
