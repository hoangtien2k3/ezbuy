
package com.ezbuy.ordermodel.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupsMemberDTO extends BaseDTO
{

    protected String accountId;
    protected String accountNo;
    protected String actStatus;
    protected String actStatusNameSubscriber;
    protected String actStatusSubscriber;
    protected String actStatusText;
    protected Date activeDatetime;
    protected XMLGregorianCalendar activeGroups;
    protected String activeStatusText;
    protected String address;
    protected String balShareCode;
    protected Long bundleConfigID;
    protected boolean checkDebit;
    protected boolean checkSendedOTP;
    protected String codeGroup;
    protected String contractNo;
    protected Date createDatetime;
    protected String createUser;
    protected Long custId;
    protected String customerName;
    protected String dataCanUse;
    protected Long dataMainSuport;
    protected String dataQuota;
    protected String dateCameraGD;
    @XmlSchemaType(name = "dateTime")
    protected Date dateReceiveOTP;
    protected BigDecimal debit;
    protected String description;
    protected boolean disablehotline;
    protected String email;
    protected Date endDatetime;
    protected Long exchangeId;
    protected String exchangeMainText;
    protected String extendDesc;
    protected boolean fellowChanged;
//    protected List<GroupsFellowNumberDTO> fellowNumbers;
    protected Long groupId;
    protected Long groupMemberId;
    protected String groupType;
    protected boolean hasPop;
    protected BigDecimal hotCharge;
    protected boolean hotline;
    protected String idNo;
    protected String idPopNo;
    protected String idType;
    protected String importStatus;
    protected String index;
    protected String indexOnFile;
    protected Long indexTrans;
    protected String isChange;
    protected String isdn;
    protected String isdnMainDataShare;
    protected List<SubAttDTO> listSubAttDTO;
//    protected List<SubGoodsDTO> listSubGoods;
    protected List<Long> listSubIdAddGroup;
//    protected List<PromotionTypeDTO> lstPromotion;
    protected String main;
    protected boolean mainDataShare;
    protected boolean mainOTP;
    protected boolean mainShare;
    protected String mainText;
    protected String mandatory;
    protected String memberPayMethod;
    protected BigDecimal needAmount;
    protected BigDecimal needToPayAmount;
    protected String needToPayAmountName;
    protected String newConnect;
    protected Long newPayLimit;
    protected String newPayMethod;
    protected int numErrorEnterOTP;
    protected Long offerId;
//    protected List<GroupsFellowNumberDTO> oldFellowNumbers;
    protected boolean oldHotline;
    protected boolean oldMain;
    protected Long oldPayLimit;
    protected String oldPayMethod;
//    protected OmniOrder omniOrder;
    protected Long orderNum;
    protected String otp;
    protected String otpCodeToCompare;
    protected String otpReceived;
    protected String password;
    protected Long payLimit;
    protected String payMethod;
    protected String payMethodText;
    protected String payType;
    protected String productCode;
    protected String promotionCode;
    protected boolean renderMainDataShare;
    protected boolean renderMainShare;
    protected boolean renderOTP;
    protected boolean renderShare;
    protected String resultAddData;
    protected boolean selectedSwitch;
    protected String shortNumber;
    protected boolean shortNumberChecked;
    protected String shortNumberValidateMsg;
    protected boolean shortNumberValidated;
    protected boolean shortNumberValidatedTrue;
    protected boolean showOtp;
    protected Date staDatetime;
    protected String status;
    protected String statusGroup;
    protected String statusGroupname;
    protected String statusInGroup;
    protected String statusNameSubscriber;
    protected String statusSub;
    protected String statusSubText;
    protected String statusSubscriber;
    protected Date subEndDatetime;
    protected Long subId;
    protected String subPromCode;
    protected Date subStaDatetime;
    protected String subType;
    protected String subscriberNumber;
    protected Long telecomServiceId;
    protected String telecomServiceName;
    protected String type;
    protected Date updateDatetime;
    protected String updateUser;
    protected String validateErrorMsg;
    protected boolean validated;
    protected boolean validatedTrue;
    protected String voiceOtherCanUse;
    protected String voiceOtherQuota;
    protected String voiceViettelCanUse;
    protected String voiceViettelQuota;
}
