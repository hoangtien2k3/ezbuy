package com.ezbuy.ordermodel.dto.sale;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerDTO extends LocationInfoDTO {

    protected String actionCheckUpdateNiceNumber;
    protected String actionCode;
    protected boolean activeMobileMoney;
    protected String allowChangeCustInfo;
    protected boolean allowCommerce;
    protected boolean allowMarket;
    protected String allowRegisterMM;
    protected boolean allowService;
    protected Date birthDate;
    protected String busPermitNo;
    protected String byPassValidateAI;
    protected String caType;
    protected boolean callRegisterChangeSim;
    protected String career;
    protected boolean checkDebit;
    protected boolean checkDoUpdateCustomer;
    protected String contactBillAddress;
    protected String contactEmail;
    protected String contactNumber;
    protected String contactTitle;
    protected boolean coppy;
    protected int countTimesCheck;
    protected Date createDatetime;
    protected boolean createFromKTR;
    protected String createUser;
    protected FullAddressDTO custAdd;
    protected CustContactDTO custContactBillAddress;
    protected String custContactBstKHDN;
    protected String custContactBstKHDNName;
    protected CustContactDTO custContactDTO;
    protected CustContactDTO custContactDTODCOM;
    protected CustContactDTO custContactDTOName;
    protected CustContactDTO custContactEmail;
    protected CustContactDTO custContactTelephone;
    protected Long custId;
    // protected CustIdentityDTO custIdentityDTO;
    protected String custType;
    protected CustTypeDTO custTypeDTO;
    protected String custTypeName;
    // @XmlElement(nillable = true)
    // protected List<UploadedFileDTO> customerFile;
    protected String description;
    protected boolean disibleIdNo;
    protected boolean exactlyCustomer;
    protected boolean executeSaleTrans;
    protected String fullPostCode;
    protected String groupCustType;
    protected String groupType;
    protected boolean haveSubActive;
    protected Date idIssueDate;
    protected String idNo;
    protected String identityNo;
    protected String identityTypeName;
    protected String idnoTypeName;
    protected boolean isNewCustomer;
    protected boolean isPCProduct;
    protected boolean isdnBeforeConnect;
    protected String isdnCheckRedis;
    protected List<CustContactDTO> listCustContact;
    // protected List<CustIdentityDTO> listCustIdentity;
    // protected List<CustIdentityDTO> listCustIdentityExist;
    // protected List<SubAttDTO> listSubAttDTO;
    // protected List<SubscriberDTO> listSubscriber;
    // protected List<ActionDetailDTO> lstActionDetail;
    protected List<String> lstBankCode;
    protected List<String> lstBankView;
    protected List<CustContactDTO> lstCustContactInfo;
    protected boolean mustValidateOldCust;
    protected boolean mustValidateWhenLoadOldCust;
    protected String name;
    protected String nationality;
    protected String nationnality;
    protected boolean needSendMess;
    protected boolean needWriteLogs;
    protected boolean newRepCustomer;
    protected boolean noSendSmsProfile;
    protected boolean noSendSmsUpdateCustomer;
    protected String notCompleteOrder;
    protected Long numSub;
    protected CustomerDTO oldInfo;
    // protected OmniOrder omniOrder;
    protected String omniOrderType;
    protected String omniProcessId;
    protected String omniTaskId;
    protected String otherIdentityNo;
    protected String payType;
    protected String popNo;
    protected String postCode;
    protected String postCodeName;
    // protected ProfileDocumentDTO profileDocumentDTO;
    protected Long reasonId;
    protected CustomerDTO receiverCust;
    protected Long referCustId;
    protected boolean regPotal;
    protected boolean renameProfile;
    protected boolean repCust;
    protected boolean repairMode;
    protected CustomerDTO representativeCust;
    protected CustomerDTO representativeCustContract;
    protected boolean requestSendOTP;
    protected String requestType;
    protected boolean runValidate;
    protected String sex;
    protected String sexName;
    // @XmlSchemaType(name = "dateTime")
    protected Date signDate;
    protected String staffApprove;
    // protected SaleStaffDTO staffDTO;
    protected String status;
    protected String street;
    // protected SubscriberDTO subscriberDTO;
    protected String systemType;
    protected String telFax;
    protected String telMobile;
    protected String telMobileContract;
    protected String tin;
    protected String tranferCustAction;
    protected String typeCustomer;
    protected boolean updateCustIdentity;
    // @XmlSchemaType(name = "dateTime")
    protected Date updateDatetime;
    protected String updateMode;
    protected String updateUser;
    protected CustomerDTO urgencyCust;
    protected CustomerDTO userInfoDTO;
    // @XmlElement(nillable = true)
    // protected List<SubExtDTO> usingCustomerInfo;
    @XmlElement(name = "VNTT")
    protected boolean vntt;

    protected String vip;
    protected String website;
    protected int workMode;
    protected LocationDTO zoneDistrict;
    protected LocationDTO zoneProvice;
}
