package com.ezbuy.ordermodel.dto.sale;

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

@XmlRootElement(name = "return")
public class ResponseCM {
    protected boolean accountE1;
    protected boolean accountNet;
    protected boolean activeNotCheckTXLL;
    protected boolean additionalPurchase;
    protected boolean allSubSipTrunkOfAccount;
    protected boolean appleWatchFree;
    protected boolean autoVerify;
    protected boolean blockSpam;
    protected boolean byPassVideoCall;
    protected boolean cancelSaleTrans;
    protected boolean changeProductWhenChangePrepaidPolicy;
    protected boolean changePromotionWhenChangePrepaidPolicy;
    protected boolean changeTelMobile;
    protected boolean checkCustExist;
    protected boolean checkCustomerInfo;
    protected boolean checkExistListIdNo;
    protected boolean checkHangSx;
    protected boolean checkIdNo;
    protected boolean checkPolice;
    protected boolean checkSecurity;
    protected String code;

    @XmlElement(name = "continue")
    protected boolean _continue;

    protected boolean custHasFtth;
    protected String description;
    protected boolean disableCancelApnDefault;
    protected boolean disableRentApnDefault;
    protected boolean enableOrgCode;
    protected boolean exactlyCustomer;
    protected boolean existCustMapNational;
    protected boolean existSubData;
    protected boolean existSubPromSpec;
    protected boolean exit;
    protected boolean hasAttSpam;
    protected boolean hasLogActionAudit;
    protected boolean hasLogOpenOneWay;
    protected boolean hasMapVContactSip;
    protected boolean hasMultiSim;
    protected boolean hasRoleByPassCheckSpam;
    protected boolean hasSubCloudCA;
    protected boolean hasUseSme;
    protected boolean mapMultiple;
    protected boolean markOwner;
    protected boolean oldCust;
    protected boolean onDatashare;
    protected boolean onOptionSetKPI;
    protected boolean pcProduct;
    protected boolean presentProfile;
    protected boolean renderTXLL;
    protected boolean resVasSafenet;
    protected boolean subInvalid;
    protected boolean subTrunk;
    protected boolean subVNTT;
    protected boolean subscriberVinfast;
    protected int totalRow;
    protected boolean trunkPstn;
    protected boolean verifyOwnerSub;
    protected List<GroupsDTO> groupsDTOList;
    protected Long numberGroupAll; // tong so luong nhom theo giay to
    protected Long numberSubAll; // tong so luong thanh vien theo dieu kien truyen vao
}
