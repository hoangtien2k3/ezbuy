package com.ezbuy.ordermodel.dto.sale;

import java.util.Date;
import java.util.List;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupsDTO extends BaseDTO {
    protected String address;
    protected String areaCode;
    protected Long bundleConfigId;
    protected boolean changeGroupsTypeSuccess;
    protected String code;
    // protected BundleGroupsConfigDTO configGroup;
    protected Date createDatetime;
    protected String createUser;
    protected CustomerDTO customerDTO;
    protected String description;
    protected String discountPromotion;
    protected String discountPromotionView;
    protected String email;
    protected Date endDatetime;
    protected String errCodeChangeGroupsType;
    // protected List<IdWithReason> failedConnectMemberList;
    protected Long groupId;
    protected String groupType;
    protected GroupsExtDTO groupsExtDTO;
    protected String home;
    protected String idPopNo;
    protected String incentiveMethod;
    protected String incentiveMethodName;
    protected String isdn;
    protected List<GroupsExtDTO> listGroupsExtDTO;
    // protected GroupsMemberDTO mainMember;
    protected List<GroupsMemberDTO> memberList;
    // protected List<SubscriberDTO> memberListSub;
    protected String messageChangeGroupsType;
    protected String name;
    protected Long productBundleGroupsConfigId;
    protected String properties;
    protected String propertiesCode;
    protected String rdRt;
    protected Long reasonId;
    protected Date staDatetime;
    protected String status;
    protected String streetName;
    // protected SubInfrastructureDTO subInfraOfMemberListSub;
    protected String telMobile;
    protected String unit;
    protected Date updateDatetime;
    protected String updateUser;
    protected String vrfName;
    protected Long numSubGroup; // tong so thanh vien cua nhom
}
